package com.order.saga.camunda.application;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableProcessApplication
public class OrderSagaApplication {

  public static void main(String... args) {
    SpringApplication.run(OrderSagaApplication.class, args);
  }

}