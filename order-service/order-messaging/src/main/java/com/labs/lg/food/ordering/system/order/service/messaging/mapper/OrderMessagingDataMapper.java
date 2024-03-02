package com.labs.lg.food.ordering.system.order.service.messaging.mapper;

import com.labs.lg.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.labs.lg.food.ordering.system.domain.valueobject.PaymentStatus;
import com.labs.lg.food.ordering.system.kafka.order.avro.model.*;
import com.labs.lg.food.ordering.system.order.service.domain.dto.message.CustomerModel;
import com.labs.lg.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.labs.lg.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.labs.lg.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.labs.lg.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderMessagingDataMapper {

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

    public PaymentRequestAvroModel orderPaymentEventToPaymentRequestAvroModel(String sagaId, OrderPaymentEventPayload orderPaymentEventPayload) {
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setCustomerId(orderPaymentEventPayload.getCustomerId())
                .setOrderId(orderPaymentEventPayload.getOrderId())
                .setPrice(orderPaymentEventPayload.getPrice())
                .setCreatedAt(orderPaymentEventPayload.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.valueOf(orderPaymentEventPayload.getPaymentOrderStatus()))
                .build();
    }

    public RestaurantApprovalRequestAvroModel
    orderApprovalEventToRestaurantApprovalRequestAvroModel(String sagaId, OrderApprovalEventPayload
            orderApprovalEventPayload) {
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setOrderId(orderApprovalEventPayload.getOrderId())
                .setRestaurantId(orderApprovalEventPayload.getRestaurantId())
                .setRestaurantOrderStatus(RestaurantOrderStatus
                        .valueOf(orderApprovalEventPayload.getRestaurantOrderStatus()))
                .setProducts(orderApprovalEventPayload.getProducts().stream().map(orderApprovalEventProduct ->
                        Product.newBuilder()
                                .setId(orderApprovalEventProduct.getId())
                                .setQuantity(orderApprovalEventProduct.getQuantity())
                                .build()).toList())
                .setPrice(orderApprovalEventPayload.getPrice())
                .setCreatedAt(orderApprovalEventPayload.getCreatedAt().toInstant())
                .build();
    }

    public CustomerModel customerAvroModelToCustomerModel(CustomerAvroModel customerAvroModel) {
        return CustomerModel.builder()
                .id(customerAvroModel.getId())
                .username(customerAvroModel.getUsername())
                .firstName(customerAvroModel.getFirstName())
                .lastName(customerAvroModel.getLastName())
                .build();
    }
}
