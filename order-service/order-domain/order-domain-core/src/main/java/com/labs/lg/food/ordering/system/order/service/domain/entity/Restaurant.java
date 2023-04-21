package com.labs.lg.food.ordering.system.order.service.domain.entity;

import com.labs.lg.food.ordering.system.domain.entity.AggregateRoot;
import com.labs.lg.food.ordering.system.domain.valueobject.RestaurantId;

import java.util.List;

public class Restaurant extends AggregateRoot<RestaurantId> {
    private final List<Product> products;
    private final boolean active;

    public Restaurant(List<Product> products, boolean active) {
        this.products = products;
        this.active = active;
    }

    public List<Product> getProducts() {
        return products;
    }

    public boolean isActive() {
        return active;
    }
}
