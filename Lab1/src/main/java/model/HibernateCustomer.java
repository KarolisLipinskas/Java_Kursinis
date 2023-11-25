package model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class HibernateCustomer {
    private EntityManagerFactory emf = null;

    public HibernateCustomer(EntityManagerFactory entityManagerFactory) {
        this.emf = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Customer customer) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(em.merge(customer));
            em.getTransaction().commit();
            System.out.println("created customer");  //delete
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Customer> getAllCustomers() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery criteriaQuery = em.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(Customer.class));
            Query query = em.createQuery(criteriaQuery);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public boolean checkCustomer(String loginName, String loginPass) {
        EntityManager em = getEntityManager();
        for (Customer e : getAllCustomers()) {
            System.out.println(e.toString());   //delete
            if (e.getUsername().equals(loginName) && e.getPassword().equals(loginPass)) return true;
        }
        em.close();
        return false;
    }
}
