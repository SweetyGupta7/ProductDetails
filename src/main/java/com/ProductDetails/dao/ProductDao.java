package com.ProductDetails.dao;

import com.ProductDetails.entity.ProductEntity;
import com.ProductDetails.entity.ProductQueueEntity;
import com.ProductDetails.model.Product;
import com.ProductDetails.model.SearchContext;
import org.hibernate.*;
import org.springframework.stereotype.Component;
import com.ProductDetails.utils.HibernateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ProductDao {

    public void createProduct(ProductEntity productEntity) {
        Transaction transaction = null;

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(productEntity);
            transaction.commit();
            System.out.println("******* commit ********");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void createProductQueue(ProductQueueEntity productQueueEntity) {
        Transaction transaction = null;

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(productQueueEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<ProductEntity> getActiveProducts() {
        Transaction transaction = null;
        List<ProductEntity> results = null;

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            String hql = "FROM ProductEntity P WHERE P.status = :status_code ORDER BY P.createdAt DESC";
            Query query = session.createQuery(hql);
            query.setParameter("status_code","active");
            //query.setMaxResults(1);
            results = query.list();
            transaction.commit();
            System.out.println("******* commit ********");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return results;
    }

    public ProductEntity getProductById(int id) {
        Transaction transaction = null;
        ProductEntity productEntity = null;

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            productEntity = (ProductEntity) session.load(ProductEntity.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return productEntity;
    }

    public List<ProductEntity> getProducts(Product product) {
        Transaction transaction = null;
        List<ProductEntity> results = null;

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            String hql = "FROM ProductEntity P WHERE P.status = :status ORDER BY P.createdAt DESC";
            Query query = session.createQuery(hql);
            query.setParameter("status","pending");
            results = query.list();
            transaction.commit();
            System.out.println("******* commit ********");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return results;
    }

    public List<ProductEntity> getProductsByQuery (SearchContext searchContext) {
        Transaction transaction = null;
        List<ProductEntity> results = null;

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            String hql = "FROM ProductEntity P WHERE P.name like :product_name and P.price >= :min_price and P.price <= :max_price and P.createdAt >= :min_posted_date and P.createdAt <= :max_posted_date ORDER BY P.createdAt DESC";
            Query query = session.createQuery(hql);
            query.setParameter("product_name","%" + searchContext.getProductName() + "%");
            query.setParameter("min_price",searchContext.getMinPrice());
            query.setParameter("max_price",searchContext.getMaxPrice());
            query.setParameter("min_posted_date",convertToDate(searchContext.getMinPostedDate()));
            query.setParameter("max_posted_date",convertToDate(searchContext.getMaxPostedDate()));
            results = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return results;
    }

    public List<ProductQueueEntity> getToBeApproveProducts() {
        Transaction transaction = null;
        List<ProductQueueEntity> results = null;

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            String hql = "FROM ProductQueueEntity P WHERE P.status = :status_code ORDER BY P.createdAt DESC";
            Query query = session.createQuery(hql);
            query.setParameter("status_code","pending");
            results = query.list();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return results;
    }

    public ProductQueueEntity getProductQueueEntityById(int id) {
        Transaction transaction = null;
        ProductQueueEntity productQueueEntity = null;

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            productQueueEntity = (ProductQueueEntity) session.load(ProductQueueEntity.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return productQueueEntity;
    }

    public int aproveProduct(int id) {
        Transaction transaction = null;
        int results = 0;

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            ProductQueueEntity productQueueEntity = (ProductQueueEntity) session.get(ProductQueueEntity.class, id);
            if(productQueueEntity != null) {
                ProductEntity productEntity = new ProductEntity();
                productEntity.setPrice(productQueueEntity.getPrice());
                productEntity.setName(productQueueEntity.getName());
                productEntity.setStatus("active");
                String hql = "DELETE FROM ProductQueueEntity "  +
                        "WHERE id = :id";
                Query query = session.createQuery(hql);
                query.setParameter("id", id);
                results = query.executeUpdate();
                session.save(productQueueEntity);
            }
            transaction.commit();
            System.out.println("******* commit ********");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return results;
    }


    public void updateProducts(Product product, int id) {
        Transaction transaction = null;

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            ProductEntity productEntity = (ProductEntity) session.load(ProductEntity.class, id);
            productEntity.setName(product.getName());
            productEntity.setPrice(product.getPrice());
            productEntity.setStatus(product.getStatus());
            productEntity.setUpdatedAt(new Date());
            session.save(productEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateProductQueueEntity(String status, int id) {
        Transaction transaction = null;

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            ProductQueueEntity productQueueEntity = (ProductQueueEntity) session.load(ProductQueueEntity.class, id);
            productQueueEntity.setStatus(status);
            productQueueEntity.setUpdatedAt(new Date());
            session.save(productQueueEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


    public int deleteProduct(int id) {
        Transaction transaction = null;
        int results = 0;

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            ProductEntity productEntity = (ProductEntity) session.get(ProductEntity.class, id);
            if(productEntity != null && !productEntity.getStatus().equalsIgnoreCase("deleted")) {
                productEntity.setStatus("deleted");
                session.save(productEntity);

                ProductQueueEntity productQueueEntity = new ProductQueueEntity();
                productQueueEntity.setProduct_id(productEntity.getId());
                productQueueEntity.setName(productEntity.getName());
                productQueueEntity.setStatus("pending");
                productQueueEntity.setPrice(productEntity.getPrice());
                productQueueEntity.setRequest_type("delete_product");
                session.save(productQueueEntity);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return results;
    }


    public int rejectProduct(int id) {
        Transaction transaction = null;
        int results = 0;

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            ProductQueueEntity productQueueEntity = (ProductQueueEntity) session.get(ProductQueueEntity.class, id);
            if(productQueueEntity != null) {
                ProductEntity productEntity = new ProductEntity();
                productEntity.setPrice(productQueueEntity.getPrice());
                productEntity.setName(productQueueEntity.getName());
                productEntity.setStatus("reject");
                String hql = "DELETE FROM ProductQueueEntity "  +
                        "WHERE id = :id";
                Query query = session.createQuery(hql);
                query.setParameter("id", id);
                results = query.executeUpdate();
                session.save(productQueueEntity);
            }
            transaction.commit();
            System.out.println("******* commit ********");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return results;
    }

    private Date convertToDate(String dateString) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
    }

}
