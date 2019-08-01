package alararestaurant.domain.entities;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity(name = "positions")
public class Position extends BaseEntityWithName {

    private Set<Employee> employees;

    @OneToMany(targetEntity = Employee.class, mappedBy = "position")
    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
