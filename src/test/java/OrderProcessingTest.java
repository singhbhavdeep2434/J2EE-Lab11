import com.example.entity.Order;
import com.example.service.OrderService;
import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
@RunWith(Arquillian.class)
public class OrderProcessingTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "com.example") // Add all application classes
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsManifestResource(new StringAsset(
                                "<jboss-deployment-structure>" +
                                        "  <deployment>" +
                                        "    <exclude-subsystems>" +
                                        "      <subsystem name=\"ejb3\" />" +
                                        "    </exclude-subsystems>" +
                                        "  </deployment>" +
                                        "</jboss-deployment-structure>"),
                        "jboss-deployment-structure.xml");
    }

    @Inject
    private OrderService orderService;

    @Test
    public void testBasicOrderCreation() {
        assertNotNull("OrderService should be injected", orderService);

        Order order = new Order();
        order.setCustomerId(1L);
        order.setProductId(100L);
        order.setQuantity(2);
        order.setTotalAmount(new BigDecimal("199.98"));

        Order result = orderService.placeOrder(order);
        assertNotNull("Order ID should be generated", result.getOrderId());
    }
}
//import com.example.entity.Order;
//import com.example.service.OrderService;
//import jakarta.inject.Inject;
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.EmptyAsset;
//import org.jboss.shrinkwrap.api.spec.WebArchive;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.math.BigDecimal;
//
//import static org.junit.Assert.*;
//
//@RunWith(Arquillian.class)
//public class OrderProcessingTest {
//
//    @Deployment
//    public static WebArchive createDeployment() {
//        return ShrinkWrap.create(WebArchive.class)
//                .addPackages(true, "com.example")
//                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
//                .addAsResource("META-INF/persistence.xml");
//    }
//
//    @Inject
//    private OrderService orderService;
//
//    @Test
//    public void testOrderProcessing() {
//        assertNotNull("OrderService should be injected", orderService);
//
//        Order order = new Order();
//        order.setCustomerId(1L);
//        order.setProductId(100L);
//        order.setQuantity(2);
//        order.setTotalAmount(new BigDecimal("199.98"));
//
//        Order placedOrder = orderService.placeOrder(order);
//        assertNotNull("Order ID should be generated", placedOrder.getOrderId());
//        assertEquals("Initial status should be Pending", "Pending", placedOrder.getStatus());
//    }
//}