package com.labs.lg.food.ordering.system.order.service.domain.entity;

import com.labs.lg.food.ordering.system.domain.valueobject.ProductId;
import com.labs.lg.pentagon.common.domain.entity.BaseEntity;
import com.labs.lg.pentagon.common.domain.valueobject.Money;

/**
 * This entity is a projection of a product,
 * but, the real entity will be into Restaurant Context
 */
@SuppressWarnings({"squid:S2160"})
public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    public Product(ProductId productId, String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }

    public Product(ProductId productId) {
        super.setId(productId);
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public void updateWithConfirmedNameAndPrice(String name, Money price) {
        this.name = name;
        this.price = price;
    }
}
