package com.labs.lg.food.ordering.system.order.service.domain;


import com.labs.lg.food.ordering.system.order.service.domain.dto.message.CustomerModel;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Customer;
import com.labs.lg.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.labs.lg.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerHelper {

    private final CustomerRepository customerRepository;
    private final OrderDataMapper orderDataMapper;

    public CustomerHelper(final CustomerRepository customerRepository,
                          final OrderDataMapper orderDataMapper) {
        this.customerRepository = customerRepository;
        this.orderDataMapper = orderDataMapper;
    }

    public void persistCustomerCreated(final CustomerModel customerModel) {
        final Customer customer = customerRepository.save(orderDataMapper.customerModelToCustomer(customerModel));
        if (customer == null) {
            log.error("Customer could not be created in Order database with id: {}", customerModel.getId());
            throw new OrderDomainException("Customer could not be created in Order database with id: "+ customerModel.getId());
        }
        log.info("Customer is created in Order database with id: {}", customer.getId().getValue());
    }
}
