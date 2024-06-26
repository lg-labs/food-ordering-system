package com.labs.lg.food.ordering.system.order.service.dataaccess.restaurant.mapper;

import com.labs.lg.food.ordering.system.order.service.domain.valueobject.ProductId;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.RestaurantId;
import com.labs.lg.food.ordering.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.labs.lg.food.ordering.system.order.service.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Product;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Restaurant;
import com.labs.lg.pentagon.common.domain.valueobject.Money;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getProducts().stream()
                .map(product -> product.getId().getValue())
                .toList();
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        final RestaurantEntity restaurantEntity = restaurantEntities.stream().findFirst().orElseThrow(() ->
                new RestaurantDataAccessException("Restaurant could not be found!"));

        final List<Product> restaurantProducts = restaurantEntities.stream()
                .map(entity ->
                        new Product(new ProductId(entity.getProductId()),
                                entity.getProductName(),
                                new Money(entity.getProductPrice())))
                .toList();
        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(restaurantProducts)
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }
}
