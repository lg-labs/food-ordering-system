package com.labs.lg.food.ordering.system.restaurant.service.domain;

import com.labs.lg.food.ordering.system.domain.valueobject.OrderId;
import com.labs.lg.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.labs.lg.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.labs.lg.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.labs.lg.food.ordering.system.restaurant.service.domain.exception.RestaurantNotFoundException;
import com.labs.lg.food.ordering.system.restaurant.service.domain.mapper.RestaurantDataMapper;
import com.labs.lg.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.labs.lg.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderRejectedMessagePublisher;
import com.labs.lg.food.ordering.system.restaurant.service.domain.ports.output.repository.OrderApprovalRepository;
import com.labs.lg.food.ordering.system.restaurant.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class RestaurantApprovalRequestHelper {

  private final RestaurantDomainService restaurantDomainService;
  private final RestaurantDataMapper restaurantDataMapper;

  private final RestaurantRepository restaurantRepository;
  private final OrderApprovalRepository orderApprovalRepository;
  private final OrderApprovedMessagePublisher orderApprovedMessagePublisher;
  private final OrderRejectedMessagePublisher orderRejectedMessagePublisher;


  public RestaurantApprovalRequestHelper(RestaurantDomainService restaurantDomainService,
                                         RestaurantDataMapper restaurantDataMapper,
                                         RestaurantRepository restaurantRepository,
                                         OrderApprovalRepository orderApprovalRepository,
                                         OrderApprovedMessagePublisher orderApprovedMessagePublisher,
                                         OrderRejectedMessagePublisher orderRejectedMessagePublisher) {
    this.restaurantDomainService = restaurantDomainService;
    this.restaurantDataMapper = restaurantDataMapper;
    this.restaurantRepository = restaurantRepository;
    this.orderApprovalRepository = orderApprovalRepository;
    this.orderApprovedMessagePublisher = orderApprovedMessagePublisher;
    this.orderRejectedMessagePublisher = orderRejectedMessagePublisher;
  }

  @Transactional
  public OrderApprovalEvent persistOrderApproved(RestaurantApprovalRequest restaurantApprovalRequest) {
    log.info("Processing restaurant for order id: {} ", restaurantApprovalRequest.orderId());
    List<String> failureMessages = new ArrayList<>();
    Restaurant restaurant = findRestaurant(restaurantApprovalRequest);

    OrderApprovalEvent orderApprovalEvent = restaurantDomainService.validateOrder(restaurant, failureMessages,
        orderApprovedMessagePublisher, orderRejectedMessagePublisher);
    orderApprovalRepository.save(orderApprovalEvent.getOrderApproval());
    return orderApprovalEvent;
  }

  private Restaurant findRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
    Restaurant restaurant = restaurantDataMapper.restaurantApprovalRequestToRestaurant(restaurantApprovalRequest);
    Optional<Restaurant> restaurantInformation = restaurantRepository.findRestaurantInformation(restaurant);
    if (restaurantInformation.isEmpty()) {
      log.error("Restaurant with id {} not found!", restaurant.getId().getValue());
      throw new RestaurantNotFoundException("Restaurant with id " + restaurant.getId().getValue() + " not found!");
    }
    Restaurant restaurantEntity = restaurantInformation.get();
    restaurant.setActive(restaurantEntity.isActive());

    restaurant.getOrderDetail().getProducts().forEach(product ->
        restaurantEntity.getOrderDetail().getProducts().forEach(productStored -> {
          if (productStored.getId().equals(product.getId())) {
            product.updateWithConfirmedNamePriceAndAvailable(productStored.getName(), productStored.getPrice(), productStored.isAvailable());
          }
        }));

    restaurant.getOrderDetail().setId(new OrderId(UUID.fromString(restaurantApprovalRequest.orderId())));

    return restaurant;
  }
}
