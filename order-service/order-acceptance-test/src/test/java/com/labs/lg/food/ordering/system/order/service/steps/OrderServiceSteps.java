package com.labs.lg.food.ordering.system.order.service.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class OrderServiceSteps {

    @Given("an order with a product not available")
    public void anOrderWithProductNotAvailable() {
        // Código para simular una orden con un producto no disponible
    }

    @When("an order request is submitted")
    public void anOrderIsSubmitted() {
        // Código para enviar la orden
    }

    @Then("the order will be created with order state {string}")
    public void theOrderWillBeCreatedWithState(String status) {
        // Código para verificar que se ha publicado un evento de creación de la orden
        // PUB(OrderCreatedEvent)
        // | topic | payment-request

        // other case:
        // PUB(OrderCreatedEvent)
        // OrderCreatedEvent
        // order state: PENDING
        //| topic
        //      | payment-request
    }

    @And("the order service will received a {string}")
    public void theOrderWillBeReceived(String status) {
        // SUB(PaymentCompletedEvent)
    }

    @And("the order service will be created a {string}")
    public void theOrderWillBeCreated(String event) {
        // the order service will be created a PUB(OrderPaidEvent)
        //        | topic
        //    | restaurant-approval-request
    }


    @Then("order service will received a {string} and {string}")
    public void orderServiceWillReceiveEventAsRejectedOrCancelled(String event, String message) {
        // Código para verificar que el servicio de órdenes recibió el evento de rechazo
        // SUB\(OrderRejectedEvent\)

        // other case
        // SUB(PaymentCancelledEvent)
        // "Processing unsuccessful payment for order id: f6139982-3749-4ade-b361-0490f9497f11 messages empty"


    }

    @Then("order service will begin with {string} as {string}")
    public void orderServiceWillBeginSaga(String saga, String sagaMessage) {
        // Código para iniciar la SAGA
        // SAGA\(OrderApprovalSaga\)

        // other case:
        // SAGA(OrderPaymentSaga)
        // output: "Cancelling order with id: f6139982-3749-4ade-b361-0490f9497f11"

        // other case:
        // "SAGA(OrderPaymentSaga)"
        // output: "Cancelling order with id: a860b9e4-632d-4dc3-97d5-2922ea264aad"
    }

    @Then("order service will change the order status to CANCELLING")
    public void orderServiceChangeStatusToCancelling() {
        // Código para cambiar el estado de la orden a CANCELANDO
    }

    @Then("order service will created an {string}")
    public void orderServiceCreatedAndOrderCancelled(String event) {
        // Código para cambiar el estado de la orden a CANCELANDO
        // PUB(OrderCancelledEvent)
        // | topic
        //      | payment-request
    }

    @Then("order service will change the order status from {string} to {string}")
    public void orderServiceChangeStatusFromCancellingToCancelled(String fromStatus, String toStatus) {
        // Código para cambiar el estado de la orden a CANCELADO
        // case 1: FromCancellingToCancelled

        // case 2:
    }

    @Then("the final status will be CANCELLED")
    public void theFinalStatusWillBeCancelled() {
        // Código para verificar el estado final
    }


    @Then("order service will begin with rollback in your system")
    public void orderServiceDoRollbackInTheSystem() {
        // Código para verificar el estado final
        // order service will begin with rollback in your system
        // clear DB checking
    }

    @Then("order service will begin with rollback with failure message")
    public void orderServiceDoneRollbackWithFailureMessage(DataTable failureMessage) {
        // more details: https://www.baeldung.com/cucumber-data-tables


    }

    @Then("order service will received a {string} with failure message")
    public void orderServiceReceivePaymentFailedEventWithFailureMessage(String event) {

    }

    @And("a new order with total price of {string}")
    public void aNewOrderWithTotalPriceOf(String arg1) {
        // create a order with Total price defined
    }

    // Otros steps relacionados con el order service...
}
