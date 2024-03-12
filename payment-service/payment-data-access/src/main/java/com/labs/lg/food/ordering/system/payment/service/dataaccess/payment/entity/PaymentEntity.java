package com.labs.lg.food.ordering.system.payment.service.dataaccess.payment.entity;

import com.labs.lg.food.ordering.system.domain.valueobject.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
@Entity
public class PaymentEntity {

    @Id
    private UUID id;
    private UUID customerId;
    private UUID orderId;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private ZonedDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentEntity that = (PaymentEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
