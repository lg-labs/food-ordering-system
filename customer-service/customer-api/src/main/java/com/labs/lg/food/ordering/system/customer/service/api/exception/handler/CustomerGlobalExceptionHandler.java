package com.labs.lg.food.ordering.system.customer.service.api.exception.handler;


import com.labs.lg.food.ordering.system.customer.service.domain.exception.CustomerDomainException;
import com.lg5.spring.api.rest.ErrorDTO;
import com.lg5.spring.api.rest.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class CustomerGlobalExceptionHandler extends GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {CustomerDomainException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(CustomerDomainException exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorDTO(HttpStatus.BAD_REQUEST.getReasonPhrase(), exception.getMessage());
    }

}
