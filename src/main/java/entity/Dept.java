package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
public class Dept extends AbstractEntity {

    @Column(nullable = false)
    private String name;
    @OneToMany(mappedBy = "dept")
    private Collection<Employee> employees;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Collection<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(final Collection<Employee> employees) {
        this.employees = employees;
    }
}
