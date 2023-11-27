package model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class HibernateCart {
    private EntityManagerFactory emf = null;

    public HibernateCart(EntityManagerFactory entityManagerFactory) {
        this.emf = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cart cart) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(em.merge(cart));
            em.getTransaction().commit();
            System.out.println("created cart");  //delete
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void update(Cart cart) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            Cart foundCart = em.find(Cart.class, cart.getId());
            foundCart.update(cart);
            em.merge(foundCart);

            em.getTransaction().commit();
            System.out.println("updated cart");  //delete
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void delete(Cart cart) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            Cart foundCart = em.find(Cart.class, cart.getId());
            em.remove(foundCart);

            em.getTransaction().commit();
            System.out.println("deleted cart");  //delete
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cart> getAllCarts() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery criteriaQuery = em.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(Cart.class));
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

    public Cart getCart(String id) {
        EntityManager em = getEntityManager();
        for (Cart e : getAllCarts()) {
            if (Integer.toString(e.getId()).equals(id)) return e;
        }
        em.close();
        return null;
    }
}
