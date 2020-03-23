
import org.hibernate.annotations.GenericGenerator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ManagedBean(name = "User")
@SessionScoped

@Entity
@Table(name = "Users")
public class User implements Serializable {

    @Id
    @GeneratedValue(generator="GENERATOR_COMMON")
    @GenericGenerator(name="GENERATOR_COMMON",strategy="increment")
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;

    //один юзер для многих точечек
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Point> points = new HashSet<Point>();

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPoints(Set<Point> points) {
        this.points = points;
    }

    public void addPoint(Point p) {
        points.add(p);
        p.setUser(this);
    }

    public void removePoint(Point p) {
        points.remove(p);
        p.setUser(null);
    }

    public Set<Point> getPoints() {
        return points;
    }
}
