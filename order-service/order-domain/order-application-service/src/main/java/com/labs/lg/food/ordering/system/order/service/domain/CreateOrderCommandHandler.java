package com.labs.lg.food.ordering.system.order.service.domain;

import com.labs.lg.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.labs.lg.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Customer;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Order;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Restaurant;
import com.labs.lg.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.labs.lg.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.labs.lg.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Component
public class CreateOrderCommandHandler {

  private final OrderDomainService orderDomainService;

  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final RestaurantRepository restaurantRepository;
  private final OrderDataMapper orderDataMapper;

  public CreateOrderCommandHandler(OrderDomainService orderDomainService,
                                   OrderRepository orderRepository,
                                   CustomerRepository customerRepository,
                                   RestaurantRepository restaurantRepository,
                                   OrderDataMapper orderDataMapper) {
    this.orderDomainService = orderDomainService;
    this.orderRepository = orderRepository;
    this.customerRepository = customerRepository;
    this.restaurantRepository = restaurantRepository;
    this.orderDataMapper = orderDataMapper;
  }

  @Transactional
  public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand){
    checkCustomer(createOrderCommand.customerId());
    Restaurant restaurant = checkRestaurant(createOrderCommand);
    Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
    OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
    Order orderResult = saveOrder(order);
    log.info("Order is created with id {}", orderResult.getId().getValue());
    return orderDataMapper.orderToCreateOrderResponse(orderResult);

  }

  private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
    Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
    Optional<Restaurant> optionalRestaurant = restaurantRepository.findRestaurantInformation(restaurant);

    if (optionalRestaurant.isEmpty()){
      log.warn("Could not find restaurant with customer id: {}", createOrderCommand.restaurantId());
      throw new OrderDomainException("Could not find restaurant with restaurant id: "+ createOrderCommand.restaurantId());
    }
    return optionalRestaurant.get();
  }

  private void checkCustomer(UUID customerId) {
    Optional<Customer> customer = customerRepository.findCustomer(customerId);
    if (customer.isEmpty()){
      log.warn("Could not find customer with customer id: {}", customerId);
      throw new OrderDomainException("Could not find customer with customer id: "+ customerId);
    }
  }

  private Order saveOrder(Order order){
    Order orderResult = orderRepository.save(order);
    if (orderResult == null){
      log.error("Could not save order with order!");
      throw new OrderDomainException("Could not save order with order!");
    }
    log.info("Order is saved with id {}", orderResult.getId().getValue());
    return orderResult;
  }
}
