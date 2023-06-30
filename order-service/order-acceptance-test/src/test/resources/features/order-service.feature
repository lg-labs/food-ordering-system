Feature:
  I as customer want create an order when send a order request

  Scenario: the order should be CANCELLED when the order has a product not available
    Given an order with a product no available
    When order is send
    Then the order will be created PUB(OrderCreatedEvent) with order state PENDING
      | topic
      | payment-request
    And payment service will received a SUB(OrderCreatedEvent)
    And the payment will be done successfully PUB(PaymentCompletedEvent)
      | topic
      | payment-response
    And the order service will received a SUB(PaymentCompletedEvent)
    And the order service will created a PUB(OrderPaidEvent)
    | topic
    | restaurant-approval-request
    And restaurant service will received a SUB(OrderPaidEvent) "Processing order approval for order id: f6139982-3749-4ade-b361-0490f9497f11"
    And restaurant service the Order will be rejected for order id: f6139982-3749-4ade-b361-0490f9497f11
    And restaurant service will changed order status as REJECTED
    And restaurant service will encapsulated in the event response a failure message as "Product with id d215b5f8-0249-4dc5-89a3-51fd148cfb47 is not available"
    And restaurant service will generated an PUB(OrderRejectedEvent)
    | topic
    | restaurant-approval-response
    And order-service will received a SUB(OrderRejectedEvent) "Processing rejected order to order id: f6139982-3749-4ade-b361-0490f9497f11, with message failed: Product with..."
    And order service will begin with SAGA(OrderApprovalSaga) as "Cancelling order with id: f6139982-3749-4ade-b361-0490f9497f11"
    And order service will change the order status to CANCELLING
    And order service will created an PUB(OrderCancelledEvent)
      | topic
      | payment-request
    And payment service will received a SUB(OrderCancelledEvent) and "Cancelling payment for order id: f6139982-3749-4ade-b361-0490f9497f11"
    And payment service will begin with rollback in your system
    And payment service will be Cancelling payment for order id: f6139982-3749-4ade-b361-0490f9497f11
    And payment service will publish an PUB(PaymentCancelledEvent)
      | topic
      | payment-response
    And order-service will received a SUB(PaymentCancelledEvent) and "Processing unsuccessful payment for order id: f6139982-3749-4ade-b361-0490f9497f11 messages empty"
    And order-service will begin with SAGA(OrderPaymentSaga) as "Cancelling order with id: f6139982-3749-4ade-b361-0490f9497f11"
    And order-service will begin with rollback in your system
    And order-service will change the order status from CANCELLING to CANCELLED
    And the final status will be CANCELLED


  Scenario: the order should be CANCELLED when is insufficient credit
    Given a customer with 500.00 of credit
    And a new order with total price of 550.00
    And the total price is more than credit, so, the customer has insufficient credit
    When send an order request
    Then order service will be created PUB(OrderCreatedEvent) with order state PENDING
      | topic
      | payment-request
    And payment service will received a SUB(OrderCreatedEvent) and "Processing payment for order id: a860b9e4-632d-4dc3-97d5-2922ea264aad"
    And payment service will check the credit given it fails
     | failure messages
     | Customer with id: a860b9e4-632d-4dc3-97d5-2922ea264aad doesn't have enough credit for payment!
     | Customer with id: d215b5f8-0249-4dc5-89a3-51fd148cfb41 doesn't have enough credit according to credit history
    And payment service will begin the credit given it fails
    And payment service will be failed and published PUB(PaymentFailedEvent)
      | topic
      | payment-response
    And order service will received a SUB(PaymentFailedEvent) with failure message
    And order-service will begin with SAGA(OrderPaymentSaga) as "Cancelling order with id: a860b9e4-632d-4dc3-97d5-2922ea264aad"
    And order-service will begin with roll backed with failure message
      | failure messages
      | Customer with id: a860b9e4-632d-4dc3-97d5-2922ea264aad doesn't have enough credit for payment!
      | Customer with id: d215b5f8-0249-4dc5-89a3-51fd148cfb41 doesn't have enough credit according to credit history
    And order-service will change the order status from PENDING to CANCELLED
    And the final status will be CANCELLED