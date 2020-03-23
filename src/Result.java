import org.hibernate.annotations.GenericGenerator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@ManagedBean(name = "Result")
@SessionScoped

@Entity
@Table(name = "Results")
public class Result implements Serializable {
    @Id
    @GeneratedValue(generator="GENERATOR_COMMON")
    @GenericGenerator(name="GENERATOR_COMMON",strategy="increment")

    @Column(name = "id")
    private int id;
    @Column(name = "r")
    private int r;
    @Column(name = "resultshot")
    private int resultShot;
    @Column(name = "thedate")
    private Date theDate;
    @ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private Point point;

    public void setPoint(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public int getId() {
        return id;
    }

    public int getR() {
        return r;
    }

    public int getResultShot() {
        return resultShot;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setResultShot(int resultShot) {
        this.resultShot = resultShot;
    }

    public void setTheDate(Date theDate) {
        this.theDate = theDate;
    }

    public Date getTheDate() {
        return theDate;
    }

    public String StringShot(){
        if (resultShot == 1) return "Попала";
        else return "Не попала";
    }

    public String StringDate(){
        SimpleDateFormat dt = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
        return dt.format(theDate);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;

        return Double.compare(result.r, r) == 0 &&
                Double.compare(result.resultShot, resultShot) == 0;
    }

    @Override
    public int hashCode() {
        int hash = Double.valueOf(r).hashCode() + Integer.valueOf(resultShot).hashCode();
        return hash;

    }
}
