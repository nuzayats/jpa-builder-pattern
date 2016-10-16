package service;

import entity.Dept;
import entity.Employee;
import entity.Project;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EmployeeServiceTest {

    private EntityManagerFactory emf;
    private EntityManager em;

    private Long engineeringDeptId;
    private Long project1Id;
    private Long project2Id;

    private EmployeeService sut;

    @Before
    public void setUp() throws Exception {
        emf = Persistence.createEntityManagerFactory("myPU");
        em = emf.createEntityManager();

        em.getTransaction().begin();

        final Dept dept = new Dept();
        dept.setName("Engineering");
        dept.setEmployees(new HashSet<>());
        em.persist(dept);

        final Project project1 = new Project();
        project1.setName("ERP integration project for Acme corp");
        project1.setEmployees(new HashSet<>());
        em.persist(project1);

        final Project project2 = new Project();
        project2.setName("Payroll system development project for Acme corp");
        project2.setEmployees(new HashSet<>());
        em.persist(project2);

        em.getTransaction().commit();

        engineeringDeptId = dept.getId();
        project1Id = project1.getId();
        project2Id = project2.getId();

        sut = new EmployeeService(em);
    }

    @Test
    public void testCreate() throws Exception {
        em.getTransaction().begin();
        final Set<Long> projectIds = new HashSet<>();
        Collections.addAll(projectIds, project1Id, project2Id);
        final Set<String> phoneNumbers = new HashSet<>();
        Collections.addAll(phoneNumbers, "000-0000-0001", "000-0000-0002", "000-0000-0003");

        final long savedEmployeeId = sut.create(
                engineeringDeptId,
                "Jane Doe",
                true,
                projectIds,
                phoneNumbers);

        em.getTransaction().commit();
        doAssert(savedEmployeeId);
    }

    @Test
    public void testBuilder() throws Exception {
        em.getTransaction().begin();

        final long savedEmployeeId = sut.builder(engineeringDeptId, "Jane Doe")
                .temporary()
                .projectIds(project1Id, project2Id)
                .phoneNumbers("000-0000-0001", "000-0000-0002", "000-0000-0003")
                .build();

        em.getTransaction().commit();
        doAssert(savedEmployeeId);
    }

    private void doAssert(long savedEmployeeId) {
        final Employee employee = em.find(Employee.class, savedEmployeeId);
        assertThat(employee.getName(), is("Jane Doe"));
        assertThat(employee.getDept().getId(), is(engineeringDeptId));
        assertThat(employee.getPhones().size(), is(3));
        assertThat(employee.getProjects().size(), is(2));
        assertThat(employee.getTemporary(), is(Boolean.TRUE));
        assertThat(em.find(Project.class, project1Id).getEmployees().iterator().next().getId(), is(savedEmployeeId));
        assertThat(em.find(Project.class, project2Id).getEmployees().iterator().next().getId(), is(savedEmployeeId));
    }

    @After
    public void tearDown() throws Exception {
        em.close();
        emf.close();
    }
}
