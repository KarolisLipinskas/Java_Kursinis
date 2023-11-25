package model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class HibernateReview {
    private EntityManagerFactory emf = null;

    public HibernateReview(EntityManagerFactory entityManagerFactory) {
        this.emf = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Review review) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(em.merge(review));
            em.getTransaction().commit();
            System.out.println("created review");  //delete
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
