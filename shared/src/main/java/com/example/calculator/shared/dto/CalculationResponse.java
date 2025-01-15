package com.example.calculator.shared.dto;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CalculationResponse {
    private BigDecimal result;
    private String error;
    
    @JsonIgnore
    private String correlationId;

    public CalculationResponse() {}

    public CalculationResponse(BigDecimal result) {
        this.result = result;
        this.error = null;
    }

    public CalculationResponse(String error) {
        this.result = null;
        this.error = error;
    }

    public CalculationResponse(BigDecimal result, String error) {
        this.result = result;
        this.error = error;
    }

    public BigDecimal getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    public void setError(String error) {
        this.error = error;
    }

    @JsonIgnore
    public boolean hasError() {
        return error != null;
    }

    @JsonIgnore
    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
} 