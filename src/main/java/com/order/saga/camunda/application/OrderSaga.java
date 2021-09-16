package com.order.saga.camunda.application;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import com.order.saga.camunda.adapter.*;
import com.order.saga.camunda.application.builder.SagaBuilder;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class OrderSaga {

    @Autowired
    private ProcessEngine camunda;

    @PostConstruct
    public void defineSaga() {
        SagaBuilder saga = SagaBuilder.newSaga("orderSaga")
                .activity("Deduct-Inventory", DeductInventoryAdapter.class)
                .compensationActivity("Reject-Deduct-Inventory", RejectDeductInventoryAdapter.class)
                .activity("Reserve-Credit", ReserveCreditAdapter.class)
                .compensationActivity("Reject-Reserve-Credit", RejectCreditReserveAdapter.class)
                .end()
                .triggerCompensationOnAnyError();

        camunda.getRepositoryService().createDeployment() //
                .addModelInstance("orderSaga.bpmn", saga.getModel()) //
                .deploy();

    }

    public void orderProduct() {
        HashMap<String, Object> someVariables = new HashMap<>();
        camunda.getRuntimeService().startProcessInstanceByKey("orderSaga", someVariables);
    }
}
