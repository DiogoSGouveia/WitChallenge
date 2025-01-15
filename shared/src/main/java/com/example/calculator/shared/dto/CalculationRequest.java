package com.example.calculator.shared.dto;

import java.math.BigDecimal;

public class CalculationRequest {
    private String operation;
    private BigDecimal a; 
    private BigDecimal b;
    private String correlationId;

    public CalculationRequest() {}

    public CalculationRequest(String operation, BigDecimal a, BigDecimal b) {
        this.operation = operation;
        this.a = a;
        this.b = b;
    }

    public String getOperation() {
        return operation;
    }
    public BigDecimal getA() {
        return a;
    }
    public BigDecimal getB() {
        return b;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
    public void setA(BigDecimal a) {
        this.a = a;
    }
    public void setB(BigDecimal b) {
        this.b = b;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
} 