package com.example.mdb;

import com.example.entity.Order;
import com.example.service.OrderRepository;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;

@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(
                        propertyName = "destinationLookup",
                        propertyValue = "java:/queue/OrderProcessingQueue"
                ),
                @ActivationConfigProperty(
                        propertyName = "destinationType",
                        propertyValue = "jakarta.jms.Queue"
                ),
                @ActivationConfigProperty(
                        propertyName = "messageSelector",
                        propertyValue = "status = 'Pending'"
                )
        }
)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class OrderProcessingMDB implements MessageListener {

    @Inject
    private OrderRepository orderRepository;

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof ObjectMessage) {
                ObjectMessage objectMessage = (ObjectMessage) message;
                Order order = (Order) objectMessage.getObject();

                System.out.println("Received order for processing: " + order.getOrderId());

                // Process the order (business logic would go here)
                // For now just update the status
                orderRepository.updateStatus(order.getOrderId(), "Processed");

                System.out.println("Successfully processed order: " + order.getOrderId());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing JMS message", e);
        }
    }
}