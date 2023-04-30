package com.labs.lg.food.ordering.system.order.service.dataaccess.order.adapter;

import com.labs.lg.food.ordering.system.order.service.dataaccess.order.entity.OrderEntity;
import com.labs.lg.food.ordering.system.order.service.dataaccess.order.mapper.OrderDataAccessMapper;
import com.labs.lg.food.ordering.system.order.service.dataaccess.order.repository.OrderJPARepository;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Order;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * <h1>DATA LAYER ADAPTER</h1>
 * <h2>Secondary Adapter</h2>
 * <p> Implements Data Interface <strong>(Output Port)</strong> from Domain Layer </p>
 *
 * @see OrderRepository
 */
@Component
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJPARepository orderJPARepository;
    private final OrderDataAccessMapper orderDataAccessMapper;

    public OrderRepositoryImpl(OrderJPARepository orderJPARepository, OrderDataAccessMapper orderDataAccessMapper) {
        this.orderJPARepository = orderJPARepository;
        this.orderDataAccessMapper = orderDataAccessMapper;
    }

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = orderDataAccessMapper.orderToOrderEntity(order);
        OrderEntity orderEntityResult = orderJPARepository.save(orderEntity);
        return orderDataAccessMapper.orderEntityToOrder(orderEntityResult);
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJPARepository.findByTrackingId(trackingId.getValue())
                .map(orderDataAccessMapper::orderEntityToOrder);
    }
}
