package model;

import model.entities.Administrator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class HibernateAdministrator {
    private EntityManagerFactory emf = null;

    public HibernateAdministrator(EntityManagerFactory entityManagerFactory) {
        this.emf = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Administrator administrator) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(em.merge(administrator));
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Administrator> getAllAdmins() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery criteriaQuery = em.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(Administrator.class));
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

    public Administrator getAdmin(String loginName, String loginPass) {
        EntityManager em = getEntityManager();
        for (Administrator e : getAllAdmins()) {
            //System.out.println(e.getClass().getName().substring(e.getClass().getName().lastIndexOf('.') + 1));
            if (e.getUsername().equals(loginName) && e.getPassword().equals(loginPass)) return e;
        }
        em.close();
        return null;
    }
}
