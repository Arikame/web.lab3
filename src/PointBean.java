import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import java.util.*;

import org.hibernate.Session;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "PointBean")
@SessionScoped

public class PointBean {

    private Session entityManager = SessionFactoryUtil.getEntityManager();

    //добавляем точку юзеру
    public void addPoint(double x, double y, int r, int shot) {

        try {
            entityManager.getTransaction().begin();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            //смотрим историю нашего пользователя
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
            Userses userses = (session != null) ? (Userses) session.getAttribute("Userses") : null;
            System.out.println("name: " + userses.getName() + " id: " + userses.getId());
            if (userses == null || userses.getId() == 0 || userses.getName() == null){
                throw new Exception();
            }
            User uses = (User)entityManager.createQuery("SELECT e from User e where e.name=:name and e.id =:id").setParameter("id",userses.getId()).setParameter("name",userses.getName()).getSingleResult();
            //работаем с точкой
            Point p = new Point();
            p.setX(x);
            p.setY(y);
            Result res = new Result();
            res.setR(r);
            res.setResultShot(shot);
            res.setTheDate(new Date());
            p.setUser(uses);
            p.addResult(res);
            uses.addPoint(p);
            entityManager.merge(uses);
            entityManager.getTransaction().commit();
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("drawPoint(" + x + "," + y + "," + r + "," + shot + ")");

        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }

    public int addClickPoint() {
        int shot = 4;
        try {
            //добавляем точку по клику на изображение
            Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            String sx = requestParameterMap.get("PointX");
            String sy = requestParameterMap.get("PointY");
            String sr = requestParameterMap.get("PointR");
            double x = Double.parseDouble(sx);
            double y = Double.parseDouble(sy);
            int r = Integer.parseInt(sr);

            addPoint(x, y, r, getShot(x, y, r));

        } catch (Exception e) {
            return shot;
        }
        return shot;
    }

    public void addPagePoint() {
        try {
            //добавляем точку/точки по отпраке формы
            Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            String sX = requestParameterMap.get("PointForm:variableX").replace(',', '.');
            String sy = requestParameterMap.get("PointForm:variableY").replace(',', '.');
            String sr = requestParameterMap.get("PointForm:variableR").replace(',', '.');
            String[] X =sX.split(" ");
            double y = Double.parseDouble(sy);
            int r = Integer.parseInt(sr);

            for (String sx : X) {
                double x = Double.parseDouble(sx);
                addPoint(x, y, r, getShot(x, y, r));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //проверка попадания
    public int getShot(double x, double y, int r) {
        if (x <= 0 && y <= 0 && x * x + y * y <= r * r || x >= 0 && y >= 0 && y <= r - x || x >= 0 && x <= r / 2.0 && y <= 0 && y >= -r)
            return 1; //Попала
        else return 2; //Не попала
    }

    //получаем список точек
    public void getList() {
        try {
            Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            String sr = requestParameterMap.get("Radius");
            int R = Integer.parseInt(sr);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
            Userses userses = (session != null) ? (Userses) session.getAttribute("Userses") : null;
            userses.setCurrentR(R);
            entityManager.getTransaction().begin();
            List<Point> pointsList = entityManager.createQuery("SELECT  e from Point e where e.user.id=:user").setParameter("user", userses.getId()).getResultList();
            if (pointsList.size() > 0) {

                RequestContext context = RequestContext.getCurrentInstance();
                for (Point pointS : pointsList) {
                    //делаем новую проверку попаданий уже для текущего радиуса
                    int itsShot = getShot(pointS.getX(), pointS.getY(), R);
                    context.execute("drawPoint(" + pointS.getX() + "," + pointS.getY() + "," + R + "," + itsShot + ")");
                    Result r = new Result();
                    r.setR(R);
                    r.setResultShot(itsShot);
                    //и дату перебиваем
                    r.setTheDate(new Date());
                    pointS.addResult(r);
                    entityManager.merge(pointS);
                }
            }
            entityManager.getTransaction().commit();
        } catch(Exception e){
            e.printStackTrace();
                entityManager.getTransaction().rollback();
            }
        }

        public List<Result> getTableList(){
        List<Result> resultList;

            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
            Userses userses = (session != null) ? (Userses) session.getAttribute("Userses") : null;

            System.out.println("Current R: " + userses.getCurrentR() + "UserName: " + userses.getName());

            entityManager.getTransaction().begin();
            //получаем лист результатов с перебитым радиусом
            resultList = entityManager.createQuery("SELECT  res from Result res where res.point.user.id=:user and res.r =:curR order by res.theDate asc").setParameter("user", userses.getId()).setParameter("curR", userses.getCurrentR()).getResultList();
            RequestContext context = RequestContext.getCurrentInstance();
            if (resultList.size() > 0) context.execute("unhidden()");
            entityManager.getTransaction().commit();
            Collections.reverse(resultList);
        return resultList;
        }
    }
