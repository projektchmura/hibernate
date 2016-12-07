

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static java.lang.System.out;


public class JpaTest {

    private EntityManager manager;

    public JpaTest(EntityManager manager) {
        this.manager = manager;
    }


    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");
        EntityManager manager = factory.createEntityManager();
        JpaTest test = new JpaTest(manager);

        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            test.createEmployeesAndDepartments();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tx.commit();

        out.println("Wyswietl tabele pracownicy");
        test.listEmployees();
        out.println("Wyswietl tabele wydzialy");
        test.listDepartament();
        out.println("Wyswietl pracownikow wydzialu kryminalnego");
        test.listEmployeeskryminalni();
    }




    private void createEmployeesAndDepartments() {
        int numOfEmployees = manager.createQuery("Select a From Employee a", Employee.class).getResultList().size();
        if (numOfEmployees == 0) {
            Department department = new Department("Wydzial kryminalny");
            manager.persist(department);
            Department department2 = new Department("Wydzial zabojstw");
            manager.persist(department2);

            manager.persist(new Employee("Igor Musiałowicz",department));
            manager.persist(new Employee("Krzysztof Musiałowicz",department));
            manager.persist(new Employee("Lena Musiałowicz",department2));
            manager.persist(new Employee("Jan Kowalski",department2));
            manager.persist(new Employee("Mara Nowakowska",department2));
            manager.persist(new Employee("Stanisław Burczymucha",department));
        }
        else
            System.out.println("coś poszło nie tak z czyszczeniem bazy");
    }


    private void listEmployees() {
        List<Employee> resultList = manager.createQuery("Select a From Employee a", Employee.class).getResultList();
        out.println("num of employess:" + resultList.size());
        for (Employee next : resultList) {
            out.println("next employee: " + next);
        }
    }

//SELECT u.user_id FROM Users u JOIN u.groups g
    private void listEmployeeskryminalni() {
        List<Employee> resultList = manager.createQuery("Select a From Employee a where a.department= '1' ", Employee.class).getResultList();
        out.println("num of employess:" + resultList.size());
        for (Employee next : resultList) {
            out.println("next employee: " + next);
        }
    }


    private void listDepartament() {
        List<Department> resultList = manager.createQuery("Select b From Department b", Department.class).getResultList();
        out.println("num of department:" + resultList.size());
        for (Department next : resultList) {
            out.println("next department: " + next);
        }
    }

}
