package com.example.rest;

import com.example.entity.Order;
import com.example.service.OrderService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/orders")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    private OrderService orderService;

    @POST
    public Response placeOrder(Order order) {
        Order placedOrder = orderService.placeOrder(order);
        return Response.status(Response.Status.CREATED).entity(placedOrder).build();
    }

    @GET
    @Path("/{id}")
    public Response getOrder(@PathParam("id") Long orderId) {
        Order order = orderService.findOrder(orderId);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(order).build();
    }

    @GET
    public Response getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return Response.ok(orders).build();
    }

    @GET
    @Path("/status/{status}")
    public Response getOrdersByStatus(@PathParam("status") String status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        return Response.ok(orders).build();
    }
}