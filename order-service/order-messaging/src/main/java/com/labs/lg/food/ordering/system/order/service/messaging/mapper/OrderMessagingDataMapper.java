package com.labs.lg.food.ordering.system.order.service.messaging.mapper;

import com.labs.lg.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.labs.lg.food.ordering.system.domain.valueobject.PaymentStatus;
import com.labs.lg.food.ordering.system.kafka.order.avro.model.*;
import com.labs.lg.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.labs.lg.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Order;
import com.labs.lg.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.labs.lg.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.labs.lg.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderMessagingDataMapper {

    public PaymentRequestAvroModel orderCreatedEventToPaymentRequestAvroModel(OrderCreatedEvent orderCreatedEvent) {
        Order order = orderCreatedEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel orderCancelledEventToPaymentRequestAvroModel(OrderCancelledEvent orderCancelledEvent) {
        Order order = orderCancelledEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCancelledEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
                .build();
    }

    public RestaurantApprovalRequestAvroModel orderPaidEventToRestaurantApprovalRequestAvroModel(OrderPaidEvent orderPaidEvent) {
        Order order = orderPaidEvent.getOrder();
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(order.getId().getValue().toString())
                .setRestaurantId(order.getRestaurantId().getValue().toString())
                .setRestaurantOrderStatus(RestaurantOrderStatus
                        .valueOf(order.getOrderStatus().name()))
                .setProducts(order.getItems().stream().map(orderItem -> Product.newBuilder()
                                .setId(orderItem.getProduct().getId().getValue().toString())
                                .setQuantity(orderItem.getQuantity())
                                .build())
                        .toList())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
                .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                .build();
    }

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentResponseAvroModel paymentResponseAvro) {
        return PaymentResponse.builder()
                .id(paymentResponseAvro.getId())
                .sagaId(paymentResponseAvro.getSagaId())
                .orderId(paymentResponseAvro.getOrderId())
                .paymentId(paymentResponseAvro.getPaymentId())
                .customerId(paymentResponseAvro.getCustomerId())
                .price(paymentResponseAvro.getPrice())
                .createdAt(paymentResponseAvro.getCreatedAt())
                .paymentStatus(PaymentStatus.valueOf(paymentResponseAvro.getPaymentStatus().name()))
                .failureMessages(paymentResponseAvro.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponse restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvro) {
        return RestaurantApprovalResponse.builder()
                .id(restaurantApprovalResponseAvro.getId())
                .sagaId(restaurantApprovalResponseAvro.getSagaId())
                .orderId(restaurantApprovalResponseAvro.getOrderId())
                .restaurantId(restaurantApprovalResponseAvro.getRestaurantId())
                .createdAt(restaurantApprovalResponseAvro.getCreatedAt())
                .orderApprovalStatus(OrderApprovalStatus
                        .valueOf(restaurantApprovalResponseAvro.getOrderApprovalStatus().name()))
                .failureMessages(restaurantApprovalResponseAvro.getFailureMessages())
                .build();
    }
}
