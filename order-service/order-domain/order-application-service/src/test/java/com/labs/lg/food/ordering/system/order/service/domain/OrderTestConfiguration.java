package com.labs.lg.food.ordering.system.order.service.domain;

import com.labs.lg.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.PaymentRequestMessagePublisher;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.RestaurantApprovalRequestMessagePublisher;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.ApprovalOutboxRepository;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.PaymentOutboxRepository;
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
  public PaymentRequestMessagePublisher paymentRequestMessagePublisher(){
    return mock(PaymentRequestMessagePublisher.class);
  }

  @Bean
  public RestaurantApprovalRequestMessagePublisher restaurantApprovalRequestMessagePublisher(){
    return mock(RestaurantApprovalRequestMessagePublisher.class);
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

  @Bean
  public PaymentOutboxRepository paymentOutboxRepository(){
    return mock(PaymentOutboxRepository.class);
  }

  @Bean
  public ApprovalOutboxRepository approvalOutboxRepository(){
    return mock(ApprovalOutboxRepository.class);
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
