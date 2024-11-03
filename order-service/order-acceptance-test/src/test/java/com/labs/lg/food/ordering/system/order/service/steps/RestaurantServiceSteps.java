package com.labs.lg.food.ordering.system.order.service.steps;

import io.cucumber.java.en.*;

public class RestaurantServiceSteps {

    @Then("restaurant service will received a {string} with {string}")
    public void restaurantServiceWillReceiveOrderPaidEvent(String event, String message) {
        // Código para verificar que el restaurante recibió el evento de orden pagada
    }

    @Then("restaurant service for the Order will be rejected for order id: {string}")
    public void restaurantServiceWillRejectOrder(String orderId) {
        // Código para simular el rechazo de la orden en el servicio de restaurante
    }

    @Then("restaurant service will changed order status as REJECTED")
    public void restaurantServiceWillChangeStatusToRejected() {
        // Código para cambiar el estado de la orden a RECHAZADA
    }

    @Then("restaurant service will encapsulated in the event response a failure message as {string}")
    public void restaurantServiceWillEncapsulateFailureMessage(String failureMessage) {
        // Código para verificar el mensaje de error encapsulado en el evento
    }

    @Then("restaurant service will generated an {string}")
    public void restaurantServiceWillGenerateOrderRejectedEvent(String event) {
        // Código para generar el evento de rechazo de la orden
        // PUB\(OrderRejectedEvent\)

    }

    // Otros steps relacionados con el servicio del restaurante...
}