package com.example.service;

import com.example.entity.Order;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Date;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class OrderRepository {

    @PersistenceContext(unitName = "orderProcessingPU")
    private EntityManager em;

    public Order create(Order order) {
        order.setOrderDate(new Date());
        em.persist(order);
        return order;
    }

    public Order find(Long orderId) {
        return em.find(Order.class, orderId);
    }

    public Order updateStatus(Long orderId, String status) {
        Order order = em.find(Order.class, orderId);
        if (order != null) {
            order.setStatus(status);
            em.merge(order);
        }
        return order;
    }

    public List<Order> findAll() {
        return em.createNamedQuery("Order.findAll", Order.class)
                .getResultList();
    }

    public List<Order> findByStatus(String status) {
        return em.createNamedQuery("Order.findByStatus", Order.class)
                .setParameter("status", status)
                .getResultList();
    }
}