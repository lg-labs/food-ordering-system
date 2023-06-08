package com.labs.lg.food.ordering.system.order.service.domain;

import com.labs.lg.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@SpringBootApplication(scanBasePackages = "com.labs.lg.food.ordering.system")
public class OrderTestConfiguration {
  /**
   * <h2>Output Ports</h2>
   *
   */
  @Bean
  public OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher(){
    return mock(OrderCreatedPaymentRequestMessagePublisher.class);
  }

  @Bean
  public OrderCancelledPaymentRequestMessagePublisher orderCancelledPaymentRequestMessagePublisher(){
    return mock(OrderCancelledPaymentRequestMessagePublisher.class);
  }

  @Bean
  public OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher(){
    return mock(OrderPaidRestaurantRequestMessagePublisher.class);
  }

  @Bean
  public OrderRepository orderRepository(){
    return mock(OrderRepository.class);
  }

  @Bean
  public CustomerRepository customerRepository(){
    return mock(CustomerRepository.class);
  }
  @Bean
  public RestaurantRepository restaurantRepository(){
    return mock(RestaurantRepository.class);
  }

  /**
   * This bean is real, isn't mock.
   * So, the OrderDomainService are in other module, and don't have spring annotation because has only business logic.
   * @return OrderDomainService the inject dependency here.
   */
  @Bean
  public OrderDomainService orderDomainService(){
    return new OrderDomainServiceImpl();
  }
}
