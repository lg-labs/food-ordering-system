package com.labs.lg.food.ordering.system.order.service.domain.entity;


import com.labs.lg.food.ordering.system.order.service.domain.valueobject.OrderId;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.labs.lg.pentagon.common.domain.entity.BaseEntity;
import com.labs.lg.pentagon.common.domain.valueobject.Money;

/**
 * Create a {@link Builder} using only FINAL parameters.
 * In others words, without {@link OrderId} field
 */
@SuppressWarnings({"squid:S2160"})
public class OrderItem extends BaseEntity<OrderItemId> {

    /**
     * {@code orderId}: isn't final, because these field will be updated later
     */
    private OrderId orderId;
    private final Product product;
    private final int quantity;
    private final Money price;
    private final Money subTotal;

    /**
     * Can make this initialize in the {@link OrderItem} method,but, the package private in the orderItem class.
     * It should only be called from {@link Order} Entity.
     * So, remove the public access in this method {@link #initializeOrderItem(OrderId, OrderItemId)}
     *
     * @param orderId     identifier the {@link Order}
     * @param orderItemId identifier the {@link OrderItem}
     */
    void initializeOrderItem(OrderId orderId, OrderItemId orderItemId) {
        this.orderId = orderId;
        super.setId(orderItemId);
    }

    boolean isPriceValid() {
        return price.isGreaterThanZero()
                && price.equals(product.getPrice())
                && price.multiply(quantity).equals(subTotal);
    }

    private OrderItem(Builder builder) {
        super.setId(builder.orderItemId);
        product = builder.product;
        quantity = builder.quantity;
        price = builder.price;
        subTotal = builder.subtotal;
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

    public Money getSubTotal() {
        return subTotal;
    }

    public static final class Builder {
        private OrderItemId orderItemId;
        private Product product;
        private int quantity;
        private Money price;
        private Money subtotal;

        private Builder() {
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

    public static Builder builder() {
        return new Builder();
    }

}
