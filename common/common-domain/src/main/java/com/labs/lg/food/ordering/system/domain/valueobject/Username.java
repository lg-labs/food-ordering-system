package com.labs.lg.food.ordering.system.domain.valueobject;

import java.util.Objects;

public class Username {
    private String value;

    public Username(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username username1 = (Username) o;
        return Objects.equals(value, username1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
