package com.labs.lg.food.ordering.system.order.service.domain.mapper;

import com.labs.lg.food.ordering.system.domain.valueobject.CustomerId;
import com.labs.lg.food.ordering.system.domain.valueobject.Money;
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
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
public class OrderDataMapper {

  public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
    return Restaurant.builder()
        .restaurantId(new RestaurantId(createOrderCommand.restaurantId()))
        .products(createOrderCommand.items().stream().map(orderItem ->
                new Product(new ProductId(orderItem.productId())))
            .collect(Collectors.toList()))
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
                .subtotal(new Money(orderItem.subtotal()))
                .build())
        .collect(Collectors.toList());
  }

  private StreetAddress orderAddressToStreetAddress(OrderAddress address) {
    return new StreetAddress(
        UUID.randomUUID(),
        address.street(),
        address.postalCode(),
        address.city());
  }

  public CreateOrderResponse orderToCreateOrderResponse(Order order) {
    return CreateOrderResponse.builder()
        .orderTrackingId(order.getTrackingId().getValue())
        .orderStatus(order.getOrderStatus())
        .build();
  }

  public TrackOrderResponse orderToTrackOrderResponse(Order order) {
    return TrackOrderResponse.builder()
        .orderStatus(order.getOrderStatus())
        .orderTrackingId(order.getTrackingId().getValue())
        .build();
  }
}