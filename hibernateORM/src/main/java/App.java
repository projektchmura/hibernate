import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.util.Iterator;
import java.util.List;

public class App {


    private static SessionFactory factory;

    public static void main(String[] args) {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        //ADD EMPLOYEE AE
        App AE = new App();
        Department department1= new Department("Sales");
        Department department2= new Department("Marketing");
        Department department3= new Department("Security");
        AE.addDepartment(department1.getName());
        AE.addDepartment(department2.getName());
        AE.addDepartment(department3.getName());


        AE.addEmployee("Jan Kowalski",department1);
        AE.addEmployee("Jan Kowalewski",department1);
        AE.addEmployee("Jan Kiepura",department2);
        AE.addEmployee("Janek Kowalski",department3);
        AE.addEmployee("Adam Niezgódka",department3);

        AE.listEmployees();

        try {
            AE.updateEmployee(4L, "Dupa Zbita");
        }catch (NullPointerException ex){
            System.out.println("Nie ma pracownika o takim ID update operation");
        }

        try {
            AE.deleteEmployee(9L);
        }catch (NullPointerException ex){
            System.out.println("Nie ma pracownika o takim ID delete operation");
        }
        AE.listEmployees();
   }

    /* Method to CREATE an employee in the database */
    public String addEmployee(String name, Department department) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Employee employee = new Employee(name, department);
            employee.setName(name);
            employee.setDepartment(department);
            session.save(employee);
  //          session.save(department);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }


    /* Method to CREATE an department in the database */
    public Department addDepartment(String name) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Department department = new Department(name);
            department.setName(name);
            session.save(department);
            tx.commit();

        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    /* Method to READ all the employees */
    public void listEmployees() {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List employees = session.createQuery("FROM Employee").list();
            for (Iterator iterator =
                 employees.iterator(); iterator.hasNext(); ) {
                Employee employee = (Employee) iterator.next();
                System.out.println("Nazwisko: " + employee.getName());
                System.out.println(" Id pracownika: " + employee.getId());
                System.out.println("Wydział: " + employee.getDepartment());
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    /* Method to UPDATE salary for an employee */
    public void updateEmployee(Long EmployeeID, String name ){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            Employee employee = (Employee)session.get(Employee.class, EmployeeID);
            employee.setName(name);
            session.update(employee);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }
 // Method to DELETE an employee from the records
    public void deleteEmployee(Long EmployeeID){
        System.out.println("***********************************************************************************");
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.delete(session.get(Employee.class, EmployeeID));
            //session.flush();
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            System.out.println("***********************************************************************************");
            session.close();
        }
    }


}


