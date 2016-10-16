package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
public class Employee extends AbstractEntity {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false, name = "temporary_emp")
    private Boolean temporary;
    @JoinColumn(nullable = false)
    @ManyToOne
    private Dept dept;
    @ManyToMany
    private Collection<Project> projects;
    @OneToMany(mappedBy = "employee")
    private Collection<Phone> phones;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Boolean getTemporary() {
        return temporary;
    }

    public void setTemporary(final Boolean temporary) {
        this.temporary = temporary;
    }

    public Dept getDept() {
        return dept;
    }

    public void setDept(final Dept dept) {
        this.dept = dept;
    }

    public Collection<Project> getProjects() {
        return projects;
    }

    public void setProjects(final Collection<Project> projects) {
        this.projects = projects;
    }

    public Collection<Phone> getPhones() {
        return phones;
    }

    public void setPhones(final Collection<Phone> phones) {
        this.phones = phones;
    }
}
