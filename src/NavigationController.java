import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(name = "NavigationController")
@RequestScoped
//bean для туда-сюда-обратно
public class NavigationController implements Serializable {

    @ManagedProperty("#{param.pageName}")

    private static final long serialVersionUID = 1L;
    private String pageName;

    public String moveToHome(){
        return "home";
    }
    public String moveToPagePoints(){
        return "page_points";
    }

    public String showPage() {

        if(pageName.equals("points_page")) {
            return "page_points";
        }else {
            return "home";
        }
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}
