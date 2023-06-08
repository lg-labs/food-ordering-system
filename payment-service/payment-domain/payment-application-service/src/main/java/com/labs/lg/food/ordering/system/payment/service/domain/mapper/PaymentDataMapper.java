package com.labs.lg.food.ordering.system.payment.service.domain.mapper;

import com.labs.lg.food.ordering.system.domain.valueobject.CustomerId;
import com.labs.lg.food.ordering.system.domain.valueobject.OrderId;
import com.labs.lg.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.labs.lg.food.ordering.system.payment.service.domain.entity.Payment;
import java.util.UUID;

import com.labs.lg.pentagon.common.domain.valueobject.Money;
import org.springframework.stereotype.Component;

@Component
public class PaymentDataMapper {

  public Payment paymentRequestModelToPayment(PaymentRequest paymentRequest) {
    return Payment
      .builder()
      .orderId(new OrderId(UUID.fromString(paymentRequest.getOrderId())))
      .customerId(
        new CustomerId(UUID.fromString(paymentRequest.getCustomerId()))
      )
      .price(new Money(paymentRequest.getPrice()))
      .build();
  }
}
