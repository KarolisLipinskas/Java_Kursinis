package model;

import model.entities.Customer;

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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void update(Customer customer) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            Customer foundCustomer = em.find(Customer.class, customer.getId());
            foundCustomer.update(customer);
            em.merge(foundCustomer);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void delete(Customer customer) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            Customer foundCustomer = em.find(Customer.class, customer.getId());
            em.remove(foundCustomer);

            em.getTransaction().commit();
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

    public Customer getCustomer(String loginName, String loginPass) {
        EntityManager em = getEntityManager();
        for (Customer e : getAllCustomers()) {
            if (e.getUsername().equals(loginName) && e.getPassword().equals(loginPass)) return e;
        }
        em.close();
        return null;
    }

    public Customer getCustomer(String id) {
        EntityManager em = getEntityManager();
        for (Customer e : getAllCustomers()) {
            if (Integer.toString(e.getId()).equals(id)) return e;
        }
        em.close();
        return null;
    }
}
