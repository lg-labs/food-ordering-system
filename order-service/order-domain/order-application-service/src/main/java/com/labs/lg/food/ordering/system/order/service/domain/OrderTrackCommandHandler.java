package com.labs.lg.food.ordering.system.order.service.domain;

import com.labs.lg.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.labs.lg.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Order;
import com.labs.lg.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.labs.lg.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.TrackingId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderTrackCommandHandler {
  private final OrderRepository orderRepository;
  private final OrderDataMapper orderDataMapper;

  public OrderTrackCommandHandler(OrderRepository orderRepository, OrderDataMapper orderDataMapper) {
    this.orderRepository = orderRepository;
    this.orderDataMapper = orderDataMapper;
  }

  public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery){
   Order order = checkTrackOrder(trackOrderQuery.orderTrackingId());
    return orderDataMapper.orderToTrackOrderResponse(order);
  }

  private Order checkTrackOrder(UUID orderTrackingId) {
    Optional<Order> orderByTrackingId = orderRepository.findByTrackingId(new TrackingId(orderTrackingId));
    if (orderByTrackingId.isEmpty()){
      log.warn("Could not find track order with track Order id: {}", orderTrackingId);
      throw new OrderDomainException("Could not find customer with customer id: "+ orderTrackingId);
    }
    return orderByTrackingId.get();
  }
}
