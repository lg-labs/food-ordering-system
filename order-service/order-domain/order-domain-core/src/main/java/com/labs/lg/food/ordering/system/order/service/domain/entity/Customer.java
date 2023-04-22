package com.labs.lg.food.ordering.system.order.service.domain.entity;

import com.labs.lg.food.ordering.system.domain.entity.AggregateRoot;
import com.labs.lg.food.ordering.system.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {

  private Customer(Builder builder) {
    super.setId(builder.customerId);
  }

  public static final class Builder {
    private CustomerId customerId;

    private Builder() {
    }

    public static Builder builder() {
      return new Builder();
    }

    public Builder customerId(CustomerId val) {
      customerId = val;
      return this;
    }

    public Customer build() {
      return new Customer(this);
    }
  }
}
