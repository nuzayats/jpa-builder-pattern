package service;

import entity.Dept;
import entity.Employee;
import entity.Phone;
import entity.Project;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EmployeeService {

    private final EntityManager em;

    EmployeeService(final EntityManager em) {
        this.em = em;
    }

    public long create(long deptId,
                       String name,
                       boolean temporary,
                       Set<Long> projectIds,
                       Set<String> phoneNumbers) {

        // instantiating and setting attributes of employee
        final Employee employee = new Employee();
        employee.setName(name);
        employee.setTemporary(temporary);

        // making a relation between employee and dept
        final Dept dept = em.find(Dept.class, deptId);
        employee.setDept(dept);
        dept.getEmployees().add(employee);

        // making relations between employee and projects
        employee.setProjects(new HashSet<>());
        for (final Long projectId : projectIds) {
            final Project project = em.find(Project.class, projectId);
            project.getEmployees().add(employee);
            employee.getProjects().add(project);
        }

        // creating phones
        employee.setPhones(new HashSet<>());
        for (final String phoneNumber : phoneNumbers) {
            final Phone phone = new Phone();
            phone.setNumber(phoneNumber);
            phone.setEmployee(employee);
            employee.getPhones().add(phone);
            em.persist(phone);
        }

        em.persist(employee);
        em.flush(); // making sure a generated id is present

        return employee.getId();
    }

    public Builder builder(long deptId, String name) {
        return new Builder(deptId, name);
    }

    public final class Builder { // not static
        private final long deptId;
        private final String name;
        private boolean temporary;
        private Set<Long> projectIds = new HashSet<>();
        private Set<String> phoneNumbers = new HashSet<>();

        public Builder(final long deptId, final String name) {
            this.deptId = deptId;
            this.name = name;
        }

        public Builder temporary() {
            temporary = true;
            return this;
        }

        public Builder projectIds(long... ids) {
            for (final long id : ids) {
                projectIds.add(id);
            }
            return this;
        }

        public Builder phoneNumbers(String... numbers) {
            phoneNumbers.addAll(Arrays.asList(numbers));
            return this;
        }

        public long build() {
            return EmployeeService.this.create(deptId, name, temporary, projectIds, phoneNumbers);
        }
    }
}
