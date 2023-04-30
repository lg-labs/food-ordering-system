package com.labs.lg.food.ordering.system.order.service.dataaccess.restaurant.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurants")
@Entity
public class RestaurantEntity {
    @Id
    private UUID id;


}
