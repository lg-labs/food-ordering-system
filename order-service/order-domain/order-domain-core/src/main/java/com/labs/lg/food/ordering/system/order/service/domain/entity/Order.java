package com.labs.lg.food.ordering.system.order.service.domain.entity;

import com.labs.lg.food.ordering.system.domain.entity.AggregateRoot;
import com.labs.lg.food.ordering.system.domain.valueobject.*;
import com.labs.lg.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.TrackingId;

import java.util.List;
import java.util.UUID;

import static com.labs.lg.food.ordering.system.domain.valueobject.OrderStatus.PENDING;

/**
 * In the {@link Order} class has any fields are not final.
 * Because these will be set during the business logic after
 * creating {@link Order} entity.
 *
 * @see <a href="https://www.notion.so/lg-labs/Section-3-Domain-Driven-Disign-DDD-1581eb27e71d42899218c8cf4b9592e9?pvs=4#6020176860a14d40b4653eeda6aa3058">Domain Exception</a>
 */
@SuppressWarnings({"squid:S2160"})
public class Order extends AggregateRoot<OrderId> {
  private final CustomerId customerId;
  private final RestaurantId restaurantId;
  private final StreetAddress deliveryAddress;
  private final Money price;
  private final List<OrderItem> items;

  private TrackingId trackingId;
  private OrderStatus orderStatus;
  private List<String> failureMessages;

  public void initializeOrder() {
    setId(new OrderId(UUID.randomUUID()));
    trackingId = new TrackingId(UUID.randomUUID());
    orderStatus = PENDING;
    initializeOrderItems();
  }

  /**
   * Validate if it has a state correct to initialize the {@link Order}
   */
  public void validateOrder() {
    validateInitialOrder();
    validateTotalPrice();
    validateItemsPrice();
  }

  public void pay() {
// default implementation ignored
  }

  public void approve() {// default implementation ignored
  }

  public void initCancel(List<String> failureMessages) {// default implementation ignored
  }

  public void cancel(List<String> failureMessages) {// default implementation ignored
  }

  /**
   * important not have any value a priori initialize {@link Order}
   *
   * @throws OrderDomainException for more information
   * @see <a href="https://www.notion.so/lg-labs/Section-3-Domain-Driven-Disign-DDD-1581eb27e71d42899218c8cf4b9592e9?pvs=4#6020176860a14d40b4653eeda6aa3058">Domain Exception</a>
   */
  private void validateInitialOrder() {
    if (orderStatus != null || getId() != null) {
      throw new OrderDomainException("Order is not in correct state to initialization!");
    }
  }

  private void validateTotalPrice() {
    if (price == null || !price.isGreaterThanZero()) {
      throw new OrderDomainException("Total price must be greater than zero!");
    }
  }

  private void validateItemsPrice() {
    Money orderItemsTotal = items.stream()
        .map(orderItem -> {
          validateItemPrice(orderItem);
          return orderItem.getSubtotal();
        })
        .reduce(Money.ZERO, Money::add);

    if (!price.equals(orderItemsTotal)) {
      throw new OrderDomainException("Total price: " + price.getAmount() +
          " is not equals to Order items total: " + orderItemsTotal.getAmount() + "!");
    }
  }


  private void validateItemPrice(OrderItem orderItem) {
    if (!orderItem.isPriceValid()) {
      throw new OrderDomainException("Order item price: " + orderItem.getPrice().getAmount() +
          " is not valid for product " + orderItem.getProduct().getId().getValue());
    }
  }

  private void initializeOrderItems() {
    long itemId = 0;
    for (OrderItem orderItem : items) {
      orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
    }
  }

  private Order(Builder builder) {
    super.setId(builder.orderId);
    customerId = builder.customerId;
    restaurantId = builder.restaurantId;
    deliveryAddress = builder.deliveryAddress;
    price = builder.price;
    items = builder.items;
    trackingId = builder.trackingId;
    orderStatus = builder.orderStatus;
    failureMessages = builder.failureMessages;
  }

  public CustomerId getCustomerId() {
    return customerId;
  }

  public RestaurantId getRestaurantId() {
    return restaurantId;
  }

  public StreetAddress getDeliveryAddress() {
    return deliveryAddress;
  }

  public Money getPrice() {
    return price;
  }

  public List<OrderItem> getItems() {
    return items;
  }

  public TrackingId getTrackingId() {
    return trackingId;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public List<String> getFailureMessages() {
    return failureMessages;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private OrderId orderId;
    private CustomerId customerId;
    private RestaurantId restaurantId;
    private StreetAddress deliveryAddress;
    private Money price;
    private List<OrderItem> items;
    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    private Builder() {
    }

    public Builder orderId(OrderId val) {
      orderId = val;
      return this;
    }

    public Builder customerId(CustomerId val) {
      customerId = val;
      return this;
    }

    public Builder restaurantId(RestaurantId val) {
      restaurantId = val;
      return this;
    }

    public Builder deliveryAddress(StreetAddress val) {
      deliveryAddress = val;
      return this;
    }

    public Builder price(Money val) {
      price = val;
      return this;
    }

    public Builder items(List<OrderItem> val) {
      items = val;
      return this;
    }

    public Builder trackingId(TrackingId val) {
      trackingId = val;
      return this;
    }

    public Builder orderStatus(OrderStatus val) {
      orderStatus = val;
      return this;
    }

    public Builder failureMessages(List<String> val) {
      failureMessages = val;
      return this;
    }

    public Order build() {
      return new Order(this);
    }
  }
}
