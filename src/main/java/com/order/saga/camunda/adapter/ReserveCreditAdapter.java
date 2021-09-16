package com.order.saga.camunda.adapter;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ReserveCreditAdapter implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("Insufficient credit");

        throw new RuntimeException("Reserve credit didnt work");

    }
}
