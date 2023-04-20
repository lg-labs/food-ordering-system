package com.labs.lg.food.ordering.system.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {
    public static final Money ZERO = new Money(BigDecimal.ZERO);
    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        this.amount = setScale(amount);
    }

    public boolean isGreaterThanZero(){
        return this.amount != null && this.amount.compareTo(BigDecimal.ZERO)>0;
    }
    public boolean isGreaterThan(Money money){
        return this.amount != null && this.amount.compareTo(money.getAmount())>0;
    }

    public Money add(Money money){
        return new Money(this.amount.add(money.getAmount()));
    }

    public Money subtract(Money subtrahend){
        return new Money(this.amount.subtract(subtrahend.getAmount()));
    }

    public Money multiply(int multiplier){
        return new Money(this.amount.multiply(new BigDecimal(multiplier)));
    }

    public BigDecimal getAmount() {
        return amount;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    /**
     * Returns with scale 2, the number of digits after decimal point is 2.
     * e.g. 10.75 or 500.80
     *
     * @apiNote with scale <em>2</em>, the number of digits after decimal point is <em>not</em>.
     * @param input {@code BigDecimal}.
     */
    private BigDecimal setScale(BigDecimal input){
        return input.setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount +
                '}';
    }
}