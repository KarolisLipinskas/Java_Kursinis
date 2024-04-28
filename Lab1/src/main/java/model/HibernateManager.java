package model;

import model.entities.Manager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class HibernateManager {
    private EntityManagerFactory emf = null;

    public HibernateManager(EntityManagerFactory entityManagerFactory) {
        this.emf = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<Manager> getAllManagers() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery criteriaQuery = em.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(Manager.class));
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

    public Manager getManager(String loginName, String loginPass) {
        EntityManager em = getEntityManager();
        for (Manager e : getAllManagers()) {
            //System.out.println(e.getClass().getName().substring(e.getClass().getName().lastIndexOf('.') + 1));
            if (e.getUsername().equals(loginName) && e.getPassword().equals(loginPass)) return e;
        }
        em.close();
        return null;
    }
}
