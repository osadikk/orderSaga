package com.order.saga.camunda.application;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {

    @Autowired
    private OrderSaga orderSaga;

    @RequestMapping(path = "/orderProduct", method = PUT)
    public void orderProduct(HttpServletResponse response) {
        orderSaga.orderProduct();
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
    }
}