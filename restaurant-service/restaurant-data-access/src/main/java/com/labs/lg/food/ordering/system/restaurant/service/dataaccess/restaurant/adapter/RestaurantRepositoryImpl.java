package com.labs.lg.food.ordering.system.restaurant.service.dataaccess.restaurant.adapter;

import com.labs.lg.food.ordering.system.dataaccess.restaurant.entity.RestaurantEntity;
import com.labs.lg.food.ordering.system.dataaccess.restaurant.repository.RestaurantJpaRepository;
import com.labs.lg.food.ordering.system.restaurant.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.labs.lg.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.labs.lg.food.ordering.system.restaurant.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {
    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    public RestaurantRepositoryImpl(RestaurantJpaRepository restaurantJpaRepository, RestaurantDataAccessMapper restaurantDataAccessMapper) {
        this.restaurantJpaRepository = restaurantJpaRepository;
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
    }

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> restaurantProducts =
                restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);
        restaurantProducts.forEach(productId -> {
            log.info("Searching the restaurant with id {} and product with id {}",
                    restaurant.getId().getValue(),
                    productId
            );

        });
        Optional<List<RestaurantEntity>> restaurantEntities = restaurantJpaRepository
                .findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(),
                        restaurantProducts);
        log.info("finished Searching the restaurants {}  {}", restaurantEntities.isPresent(), restaurantEntities.get().size());

        return restaurantEntities.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }
}
