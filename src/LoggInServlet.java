import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@ManagedBean(name = "LoggIn")
@SessionScoped

//bean-обработчик перехода со стартовой страницы
public class LoggInServlet {
    //используем Entety Manager для получения Hibernate session
    private  Session entityManager = SessionFactoryUtil.getEntityManager();
    //функция-обработчик введенного никнейма
    public void loggin() {
        //Создаем 3 строчки для инициализации 3 таблиц:
        // для сохранения пользователей, точек и результатов
        String createPOINTS = new String("Create TABLE POINTS(id SERIAL PRIMARY KEY," +
                "X real," +
                "Y real," +
                "user_id real)");
        String createUSERS = new String("Create TABLE USERS(id SERIAL PRIMARY KEY, name varchar(255))");
        String createRESULTS = new String("Create TABLE results(id SERIAL PRIMARY KEY," +
                "R real," +
                "resultshot real," +
                "thedate real," +
                "point_id real)");
        //считываем Faces контекст
        FacesContext facesContext = FacesContext.getCurrentInstance();
        //получаем из него историю запросов http
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        //получаем оттуда атрибут Userses
        Userses userses =  (Userses) session.getAttribute("Userses");
        try {
            //считываем параметры запроса в виде Map
            Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            //считываем из формы с id CreateLoggin параметр name
            String sName = requestParameterMap.get("CreateLoggin:name");
            System.out.println("sid: " + sName);
            //создаем нового Юзера
            User user = new User();
            user.setName(sName);
            //начинаем транзакцию
            entityManager.getTransaction().begin();
            User us = null;
            //создаем экземпляр CriteriaBuilder для создания объектов запросов
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> cquery = builder.createQuery(User.class);
            //создаем корневой объект, с которого будем обходить дерево свойств
            Root<User> root = cquery.from( User.class );
            cquery.select( root );
            //прописываем, что хотим найти в бд
            cquery.where(builder.equal( root.get("name"), sName ));
            try {
                //отправляем запрос через Entety Manager
                us = (User) entityManager.createQuery(cquery).getSingleResult();
            }
            catch (NoResultException e){
                us = null;
            }
            if (us != null){
                //Находим нашего Юзера в бд и присваиваем его параметры текущему объекту Userses
                userses.setId(us.getId());
                userses.setName(us.getName());
            }
            else {
                //если же искомая сущность detached, все равно извлекаем то, что нам надо
                user = (User)entityManager.merge(user);
                userses.setId(user.getId());
                userses.setName(user.getName());
            }
            System.out.println("USER ID:" + userses.getId());
            //завершаем транзакцию
            entityManager.getTransaction().commit();
            //переходим на страницу с точками
            facesContext.getExternalContext().redirect("points_page.xhtml");
        } catch (Exception e) {
            //если все сломалось, отзываем транзакцию
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }
}