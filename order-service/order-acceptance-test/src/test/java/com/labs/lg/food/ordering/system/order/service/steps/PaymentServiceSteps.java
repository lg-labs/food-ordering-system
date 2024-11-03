package com.labs.lg.food.ordering.system.order.service.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;

public class PaymentServiceSteps {

    @Then("payment service will received a OrderCreatedEvent")
    public void paymentServiceWillReceiveOrderCreatedEvent() {
        // Código para verificar que el servicio de pagos recibió el evento de creación de la orden
        //  SUB(OrderCreatedEvent)
    }

    @Then("the payment will be completed successfully")
    public void paymentWillBeDoneSuccessfully() {
        // Código para verificar que el evento de pago exitoso fue publicado
        // PUB(PaymentCompletedEvent)
        //| topic
      //| payment-response
    }

    @Then("payment service will received a {string} and {string}")
    public void paymentServiceWillReceiveOrderCancelledEvent(String event, String message) {
        // Código para verificar que el servicio de pagos recibió el evento de cancelación
        // SUB\(OrderCancelledEvent\)
        // output -> "Cancelling payment for order id: f6139982-3749-4ade-b361-0490f9497f11"

        // other case:
        // SUB(OrderCreatedEvent)
        // output: Processing payment for order id: a860b9e4-632d-4dc3-97d5-2922ea264aad


    }

    @Then("payment service will begin with rollback in your system")
    public void paymentServiceWillBeginWithRollback() {
        // Código para iniciar el rollback en el servicio de pagos
    }

    @Then("payment service will be Cancelling payment for order id: {string}")
    public void paymentServiceCancellingPaymentForOrderId(String orderId) {
        // Código para simular la cancelación de pago por el servicio de pagos
    }

    @Then("payment service will publish an {string}")
    public void paymentServiceWillPublishPaymentCancelledEvent(String event) {
        // Código para publicar el evento de cancelación del pago
        // PUB\(PaymentCancelledEvent
        // | topic
        //      | payment-response
    }

    @Then("payment service will check the credit given it fails")
    public void paymentServiceCheckCreditFails(DataTable failureMessage) {
        // Código para verificar que el chequeo de crédito falla
    }

    @Then("payment service will be failed and published {string}")
    public void paymentServiceWillFailAndPublishFailedEvent(String event) {
        // Código para verificar que el servicio de pagos falló y publicó el evento fallido
        // PUB\(PaymentFailedEvent\)
        //       | topic
        //      | payment-response
    }

    @Then("payment service will begin the credit given it fails")
    public void paymentServiceBeginTheCreditFails() {
        //
    }

    @And("the total price is more than credit,so, the customer has insufficient credit")
    public void theTotalPriceIsMoreThanCreditSoTheCustomerHasInsufficientCredit() {

    }

    @Given("a customer with {string} of credit")
    public void aCustomerWithOfCredit(String arg0) {
        // Configurar el credito del cliente al valor definido
    }


    // Otros steps relacionados con el servicio de pagos...
}