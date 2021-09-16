package com.order.saga.camunda.application.builder;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.builder.AbstractActivityBuilder;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.builder.ProcessBuilder;

public class SagaBuilder {

    private AbstractFlowNodeBuilder saga;
    private BpmnModelInstance bpmnModelInstance;
    private String name;
    private ProcessBuilder process;

    public SagaBuilder(String name) {
        this.name = name;
    }

    public static SagaBuilder newSaga(String name) {
        SagaBuilder builder = new SagaBuilder(name);
        return builder.start();
    }

    public BpmnModelInstance getModel() {
        if (bpmnModelInstance==null) {
            bpmnModelInstance = saga.done();
        }
        return bpmnModelInstance;
    }

    public SagaBuilder start() {
        process = Bpmn.createExecutableProcess(name);
        saga = process.startEvent("Start-" + name);
        return this;
    }

    public SagaBuilder end() {
        saga = saga.endEvent("End-" + name);
        return this;
    }

    @SuppressWarnings("rawtypes")
    public SagaBuilder activity(String name, Class adapterClass) {
        String id = "Activity-" + name;
        saga = saga.serviceTask(id).name(name).camundaClass(adapterClass.getName());
        return this;
    }

    public SagaBuilder compensationActivity(String name, Class adapterClass) {
        if (!(saga instanceof AbstractActivityBuilder)) {
            throw new RuntimeException("Compensation activity can only be specified right after activity");
        }

        String id = "Activity-" + name + "-compensation";

        ((AbstractActivityBuilder)saga)
                .boundaryEvent()
                .compensateEventDefinition()
                .compensateEventDefinitionDone()
                .compensationStart()
                .serviceTask(id).name(name).camundaClass(adapterClass.getName())
                .compensationDone();

        return this;
    }

    public SagaBuilder triggerCompensationOnAnyError() {
        process.eventSubProcess()
                .startEvent("ErrorCatched").error("java.lang.Throwable")
                .intermediateThrowEvent("ToBeCompensated").compensateEventDefinition().compensateEventDefinitionDone()
                .endEvent("ErrorHandled");

        return this;
    }

}