package alararestaurant.domain.dtos.employee;

import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class ImportEmployeeJson {

    @Expose
    private String name;

    @Expose
    private Integer age;

    @Expose
    private String position;

    @Size(min = 3, max = 30)
    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Min(15)
    @Max(80)
    @Column(nullable = false)
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Size(min = 3, max = 30)
    @Column(nullable = false)
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
