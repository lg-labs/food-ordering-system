package com.labs.lg.food.ordering.system.order.service.domain.entity;

import com.labs.lg.food.ordering.system.domain.entity.BaseEntity;
import com.labs.lg.food.ordering.system.domain.valueobject.Money;
import com.labs.lg.food.ordering.system.domain.valueobject.OrderId;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.OrderItemId;

/**
 * Create a {@link Builder} using only FINAL parameters.
 * In others words, without {@link OrderId} field
 */
public class OrderItem extends BaseEntity<OrderItemId> {

    /**
     * {@code orderId}: isn't final, because these field will be updated later
     */
    private OrderId orderId;
    private final Product product;
    private final int quantity;
    private final Money price;
    private final Money subtotal;

    private OrderItem(Builder builder) {
        super.setId(builder.orderItemId);
        product = builder.product;
        quantity = builder.quantity;
        price = builder.price;
        subtotal = builder.subtotal;
    }


    public OrderId getOrderId() {
        return orderId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }

    public Money getSubtotal() {
        return subtotal;
    }

    public static final class Builder {
        private OrderItemId orderItemId;
        private Product product;
        private int quantity;
        private Money price;
        private Money subtotal;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder orderItemId(OrderItemId val) {
            orderItemId = val;
            return this;
        }

        public Builder product(Product val) {
            product = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder subtotal(Money val) {
            subtotal = val;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}
