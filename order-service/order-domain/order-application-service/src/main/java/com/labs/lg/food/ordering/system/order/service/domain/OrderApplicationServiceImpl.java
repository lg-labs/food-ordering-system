package com.labs.lg.food.ordering.system.order.service.domain;

import com.labs.lg.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.labs.lg.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.labs.lg.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.labs.lg.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.labs.lg.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Slf4j
@Validated
@Service
class OrderApplicationServiceImpl implements OrderApplicationService {

  private final CreateOrderCommandHandler createOrderCommandHandler;
  private final OrderTrackCommandHandler orderTrackCommandHandler;

  public OrderApplicationServiceImpl(CreateOrderCommandHandler createOrderCommandHandler,
                                     OrderTrackCommandHandler orderTrackCommandHandler) {
    this.createOrderCommandHandler = createOrderCommandHandler;
    this.orderTrackCommandHandler = orderTrackCommandHandler;
  }

  @Override
  public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand){
    return createOrderCommandHandler.createOrder(createOrderCommand);
  }
  @Override
  public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery){
    return orderTrackCommandHandler.trackOrder(trackOrderQuery);
  }
}
