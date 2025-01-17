package com.example.calculator.shared.dto;

import java.math.BigDecimal;

/*
CalculationRequest is a data transfer object (DTO) that represents a request for a calculation.
It contains the operation to be performed, the two operands, and a request ID for correlation purposes.
*/

public class CalculationRequest {
    private String operation;
    private BigDecimal a; 
    private BigDecimal b;
    private String requestId;

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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
} 