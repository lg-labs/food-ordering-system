package com.labs.lg.food.ordering.system.order.service.domain.mapper;

import com.labs.lg.food.ordering.system.domain.valueobject.CustomerId;

import com.labs.lg.food.ordering.system.domain.valueobject.PaymentOrderStatus;
import com.labs.lg.food.ordering.system.domain.valueobject.ProductId;
import com.labs.lg.food.ordering.system.domain.valueobject.RestaurantId;
import com.labs.lg.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.labs.lg.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.labs.lg.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.labs.lg.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Order;
import com.labs.lg.food.ordering.system.order.service.domain.entity.OrderItem;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Product;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Restaurant;
import com.labs.lg.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.labs.lg.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.labs.lg.pentagon.common.domain.valueobject.Money;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Component
public class OrderDataMapper {

  public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
    return Restaurant.builder()
        .restaurantId(new RestaurantId(createOrderCommand.restaurantId()))
        .products(createOrderCommand.items().stream().map(orderItem ->
                new Product(new ProductId(orderItem.productId())))
            .toList())
        .build();
  }

  public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
    return Order.builder()
        .customerId(new CustomerId(createOrderCommand.customerId()))
        .restaurantId(new RestaurantId(createOrderCommand.restaurantId()))
        .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.address()))
        .price(new Money(createOrderCommand.price()))
        .items(orderItemsToOrderItemEntity(createOrderCommand.items()))
        .build();
  }

  private List<OrderItem> orderItemsToOrderItemEntity(List<com.labs.lg.food.ordering.system.order.service.domain.dto.create.OrderItem> items) {
    return items.stream()
        .map(orderItem ->
            OrderItem.builder()
                .product(new Product(new ProductId(orderItem.productId())))
                .price(new Money(orderItem.price()))
                .quantity(orderItem.quantity())
                .subtotal(new Money(orderItem.subTotal()))
                .build())
        .toList();
  }

  private StreetAddress orderAddressToStreetAddress(OrderAddress address) {
    return new StreetAddress(
        UUID.randomUUID(),
        address.street(),
        address.postalCode(),
        address.city());
  }

  public CreateOrderResponse orderToCreateOrderResponse(Order order, String message) {
    return CreateOrderResponse.builder()
        .orderTrackingId(order.getTrackingId().getValue())
        .orderStatus(order.getOrderStatus())
        .message(message)
        .build();
  }

  public TrackOrderResponse orderToTrackOrderResponse(Order order) {
    return TrackOrderResponse.builder()
        .orderTrackingId(order.getTrackingId().getValue())
        .orderStatus(order.getOrderStatus())
        .failureMessages(order.getFailureMessages())
        .build();
  }

  public OrderPaymentEventPayload orderCreatedEventToOrderPaymentEventPayload(OrderCreatedEvent orderCreatedEvent) {
    return OrderPaymentEventPayload.builder()
            .customerId(orderCreatedEvent.getOrder().getCustomerId().getValue().toString())
            .orderId(orderCreatedEvent.getOrder().getId().getValue().toString())
            .price(orderCreatedEvent.getOrder().getPrice().getAmount())
            .createdAt(orderCreatedEvent.getCreatedAt())
            .paymentOrderStatus(PaymentOrderStatus.PENDING.name())
            .build();
  }
}
