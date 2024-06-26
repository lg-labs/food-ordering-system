package com.labs.lg.food.ordering.system.order.service.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labs.lg.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.labs.lg.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.labs.lg.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.labs.lg.food.ordering.system.order.service.domain.dto.create.OrderItem;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Customer;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Order;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Product;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Restaurant;
import com.labs.lg.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.labs.lg.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.labs.lg.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.labs.lg.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.labs.lg.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.ApprovalOutboxRepository;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.PaymentOutboxRepository;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import com.labs.lg.food.ordering.system.order.service.domain.saga.SagaStatus;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.CustomerId;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.OrderId;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.OrderStatus;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.PaymentOrderStatus;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.ProductId;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.RestaurantId;
import com.labs.lg.pentagon.common.domain.valueobject.Money;
import com.lg5.spring.outbox.OutboxStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.labs.lg.food.ordering.system.order.service.domain.saga.SagaConstants.ORDER_SAGA_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
class OrderApplicationServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;
    @Autowired
    private OrderDataMapper orderDataMapper;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private PaymentOutboxRepository paymentOutboxRepository;
    @Autowired
    private ApprovalOutboxRepository approvalOutboxRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;

    private final UUID CUSTOMER_ID = UUID.fromString("97e0a6c1-91bb-4c07-9647-6e008cadb100");
    private final UUID RESTAURANT_ID = UUID.fromString("5c2a57dd-d45b-41a5-b4c6-33735c9c9d3a");
    private final UUID PRODUCT_ID_1 = UUID.fromString("d0ae8284-de7f-47ad-98bd-5364afebb961");
    private final UUID PRODUCT_ID_2 = UUID.fromString("d0ae8284-de7f-47ad-98bd-5364afebb962");
    private final UUID ORDER_ID = UUID.fromString("3db9f0ab-581a-4c79-85c5-2889fa26a468");
    private final UUID SAGA_ID = UUID.fromString("3db9f0ab-581a-4c79-85c5-2889fa26aafa");
    private final BigDecimal PRICE = new BigDecimal("200.00");

    @BeforeEach
    public void init() {
        createOrderCommand = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street_1")
                        .postalCode("1000A")
                        .city("Paris")
                        .build())
                .price(PRICE)
                .items(List.of(OrderItem.builder()
                                .productId(PRODUCT_ID_1)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID_2)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()

                ))
                .build();

        createOrderCommandWrongPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street_1")
                        .postalCode("1000A")
                        .city("Paris")
                        .build())
                .price(new BigDecimal("250.00"))
                .items(List.of(OrderItem.builder()
                                .productId(PRODUCT_ID_1)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID_2)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()

                ))
                .build();

        createOrderCommandWrongProductPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street_1")
                        .postalCode("1000A")
                        .city("Paris")
                        .build())
                .price(new BigDecimal("210.00"))
                .items(List.of(OrderItem.builder()
                                .productId(PRODUCT_ID_1)
                                .quantity(1)
                                .price(new BigDecimal("60.00"))
                                .subTotal(new BigDecimal("60.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID_2)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()
                ))
                .build();

        Customer customer = new Customer(new CustomerId(CUSTOMER_ID));

        Restaurant restaurantResponse = Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.restaurantId()))
                .products(List.of(
                        new Product(new ProductId(PRODUCT_ID_1), "product-1",
                                new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID_2), "product-2",
                                new Money(new BigDecimal("50.00")))
                ))
                .active(true)
                .build();

        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));


        when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(restaurantRepository
                .findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(paymentOutboxRepository.save(any(OrderPaymentOutboxMessage.class))).thenReturn(getOrderPaymentOutboxMessage());

    }

    @Test
    void testCreateOrder() {
        CreateOrderResponse order = orderApplicationService.createOrder(createOrderCommand);
        assertEquals(OrderStatus.PENDING, order.orderStatus());
        assertEquals("Order created successfully", order.message());
        assertNotNull(order.orderTrackingId());
    }

    @Test
    void createOrderCommandWrongTotalPrice() {
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongPrice));
        assertEquals("Total price: 250.00 is not equal to Order items total: 200.00!", orderDomainException.getMessage());

    }

    @Test
    void createOrderCommandWrongProductPrice() {
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongProductPrice));
        assertEquals("Order item price: 60.00 is not valid for product " + PRODUCT_ID_1, orderDomainException.getMessage());
    }

    @Test
    void createOrderWithPassiveRestaurant() {
        Restaurant restaurantResponse_aux = Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.restaurantId()))
                .products(List.of(
                        new Product(new ProductId(PRODUCT_ID_1), "product-1",
                                new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID_2), "product-2",
                                new Money(new BigDecimal("50.00")))
                ))
                .active(false)
                .build();

        when(restaurantRepository
                .findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand))
        ).thenReturn(Optional.of(restaurantResponse_aux));
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommand));


        assertEquals("Restaurant with Id "
                + restaurantResponse_aux.getId().getValue()
                + "is currently no active", orderDomainException.getMessage());
    }


    private OrderPaymentOutboxMessage getOrderPaymentOutboxMessage() {
        OrderPaymentEventPayload orderPaymentEventPayload = OrderPaymentEventPayload.builder()
                .orderId(ORDER_ID.toString())
                .customerId(CUSTOMER_ID.toString())
                .price(PRICE)
                .createdAt(ZonedDateTime.now())
                .paymentOrderStatus(PaymentOrderStatus.PENDING.name())
                .build();
        return OrderPaymentOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(SAGA_ID)
                .createdAt(ZonedDateTime.now())
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(orderPaymentEventPayload))
                .orderStatus(OrderStatus.PENDING)
                .sagaStatus(SagaStatus.STARTED)
                .outboxStatus(OutboxStatus.STARTED)
                .version(0)
                .build();
    }

    private String createPayload(OrderPaymentEventPayload orderPaymentEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderPaymentEventPayload);
        } catch (Exception e) {
            throw new OrderDomainException("Cannot create OrderPaymentEventPayload object!");
        }
    }

}


















