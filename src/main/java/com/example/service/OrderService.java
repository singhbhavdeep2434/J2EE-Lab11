package com.example.service;

import com.example.entity.Order;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageProducer;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.Session;

import java.util.List;

@Stateless
public class OrderService {

    @Inject
    private OrderRepository orderRepository;

    @Resource(lookup = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/queue/OrderProcessingQueue")
    private Queue orderQueue;

    public Order placeOrder(Order order) {
        // Persist the order
        Order createdOrder = orderRepository.create(order);

        // Send to processing queue
        sendToProcessingQueue(createdOrder);

        return createdOrder;
    }

    private void sendToProcessingQueue(Order order) {
        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
             MessageProducer producer = session.createProducer(orderQueue)) {

            ObjectMessage message = session.createObjectMessage(order);
            message.setStringProperty("status", order.getStatus());
            producer.send(message);

            System.out.println("Sent order to processing queue: " + order.getOrderId());

        } catch (JMSException e) {
            throw new RuntimeException("Failed to send order to processing queue", e);
        }
    }

    public Order findOrder(Long orderId) {
        return orderRepository.find(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
}