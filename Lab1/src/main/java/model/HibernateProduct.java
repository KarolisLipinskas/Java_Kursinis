package model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class HibernateProduct {
    private EntityManagerFactory emf = null;

    public HibernateProduct(EntityManagerFactory entityManagerFactory) {
        this.emf = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Product product) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(em.merge(product));
            em.getTransaction().commit();
            System.out.println("created product");  //delete
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Product> getAllProducts() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery criteriaQuery = em.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(Product.class));
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

    public Product getProduct(int id) {
        EntityManager em = getEntityManager();
        for (Product product : getAllProducts()) {
            if (product.getId() == id) return product;
        }
        em.close();
        return null;
    }
}
