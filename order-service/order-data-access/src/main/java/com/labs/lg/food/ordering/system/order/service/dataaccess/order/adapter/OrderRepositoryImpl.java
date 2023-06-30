package com.labs.lg.food.ordering.system.order.service.dataaccess.order.adapter;

import com.labs.lg.food.ordering.system.domain.valueobject.OrderId;
import com.labs.lg.food.ordering.system.order.service.dataaccess.order.mapper.OrderDataAccessMapper;
import com.labs.lg.food.ordering.system.order.service.dataaccess.order.repository.OrderJpaRepository;
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

    private final OrderJpaRepository orderJPARepository;
    private final OrderDataAccessMapper orderDataAccessMapper;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, OrderDataAccessMapper orderDataAccessMapper) {
        this.orderJPARepository = orderJpaRepository;
        this.orderDataAccessMapper = orderDataAccessMapper;
    }

    @Override
    public Order save(Order order) {
        return orderDataAccessMapper.orderEntityToOrder(orderJPARepository
            .save(orderDataAccessMapper.orderToOrderEntity(order)));
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJPARepository.findByTrackingId(trackingId.getValue())
                .map(orderDataAccessMapper::orderEntityToOrder);
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return orderJPARepository.findById(orderId.getValue()).map(orderDataAccessMapper::orderEntityToOrder);
    }
}
