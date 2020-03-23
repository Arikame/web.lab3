import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean(name="Userses")
@SessionScoped
//храним текущих пользователя и радиус
public class Userses implements Serializable {
    private int id;
    private String name;

    private int currentR;

    public void setCurrentR(int currentR) {
        this.currentR = currentR;
    }

    public int getCurrentR() {
        return currentR;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String validation(){
        if (name == null) return "fail";
        else return "success";
    }
}
