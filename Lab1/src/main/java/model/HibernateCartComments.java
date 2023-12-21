package model;

import model.entities.Cart;
import model.entities.CartComment;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class HibernateCartComments {
    private EntityManagerFactory emf = null;

    public HibernateCartComments(EntityManagerFactory entityManagerFactory) {
        this.emf = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CartComment cartComment) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(em.merge(cartComment));
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void delete(CartComment cartComment) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            CartComment foundCartComment = em.find(CartComment.class, cartComment.getId());
            em.remove(foundCartComment);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CartComment> getAllCartComments() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery criteriaQuery = em.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(CartComment.class));
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

    public CartComment getCartComment(String id) {
        EntityManager em = getEntityManager();
        for (CartComment e : getAllCartComments()) {
            if (Integer.toString(e.getId()).equals(id)) return e;
        }
        em.close();
        return null;
    }

    public CartComment getLastComment(int cartId) {
        CartComment cartComment = null;
        EntityManager em = getEntityManager();
        for (CartComment e : getAllCartComments()) {
            if (e.getCart() != null && e.getCart().getId() == cartId) cartComment = e;
        }
        em.close();
        return cartComment;
    }
}
