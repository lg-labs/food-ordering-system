package com.labs.lg.food.ordering.system.restaurant.service.domain;

import com.labs.lg.food.ordering.system.domain.valueobject.OrderId;
import com.labs.lg.food.ordering.system.outbox.OutboxStatus;
import com.labs.lg.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.labs.lg.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.labs.lg.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.labs.lg.food.ordering.system.restaurant.service.domain.exception.RestaurantNotFoundException;
import com.labs.lg.food.ordering.system.restaurant.service.domain.mapper.RestaurantDataMapper;
import com.labs.lg.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import com.labs.lg.food.ordering.system.restaurant.service.domain.outbox.scheduler.OrderOutboxHelper;
import com.labs.lg.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.RestaurantApprovalResponseMessagePublisher;
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
    private final OrderOutboxHelper orderOutboxHelper;
    private final RestaurantApprovalResponseMessagePublisher restaurantApprovalResponseMessagePublisher;


    public RestaurantApprovalRequestHelper(RestaurantDomainService restaurantDomainService,
                                           RestaurantDataMapper restaurantDataMapper,
                                           RestaurantRepository restaurantRepository,
                                           OrderApprovalRepository orderApprovalRepository,
                                           OrderOutboxHelper orderOutboxHelper,
                                           RestaurantApprovalResponseMessagePublisher
                                                   restaurantApprovalResponseMessagePublisher) {
        this.restaurantDomainService = restaurantDomainService;
        this.restaurantDataMapper = restaurantDataMapper;
        this.restaurantRepository = restaurantRepository;
        this.orderApprovalRepository = orderApprovalRepository;
        this.orderOutboxHelper = orderOutboxHelper;
        this.restaurantApprovalResponseMessagePublisher = restaurantApprovalResponseMessagePublisher;
    }

    @Transactional
    public void persistOrderApproved(RestaurantApprovalRequest restaurantApprovalRequest) {
        if (publishIfOutboxMessageProcessed(restaurantApprovalRequest)) {
            log.info("An outbox message with saga id: {} already saved to database!",
                    restaurantApprovalRequest.sagaId());
            return;
        }

        log.info("Processing restaurant approval for order id: {}", restaurantApprovalRequest.orderId());
        List<String> failureMessages = new ArrayList<>();
        Restaurant restaurant = findRestaurant(restaurantApprovalRequest);
        OrderApprovalEvent orderApprovalEvent =
                restaurantDomainService.validateOrder(
                        restaurant,
                        failureMessages);
        orderApprovalRepository.save(restaurant.getOrderApproval());

        orderOutboxHelper
                .saveOrderOutboxMessage(restaurantDataMapper.orderApprovalEventToOrderEventPayload(orderApprovalEvent),
                        orderApprovalEvent.getOrderApproval().getApprovalStatus(),
                        OutboxStatus.STARTED,
                        UUID.fromString(restaurantApprovalRequest.sagaId()));
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

    private boolean publishIfOutboxMessageProcessed(RestaurantApprovalRequest restaurantApprovalRequest) {
        Optional<OrderOutboxMessage> orderOutboxMessage =
                orderOutboxHelper.getCompletedOrderOutboxMessageBySagaIdAndOutboxStatus(UUID
                        .fromString(restaurantApprovalRequest.sagaId()), OutboxStatus.COMPLETED);
        if (orderOutboxMessage.isPresent()) {
            restaurantApprovalResponseMessagePublisher.publish(orderOutboxMessage.get(),
                    orderOutboxHelper::updateOutboxStatus);
            return true;
        }
        return false;
    }
}
