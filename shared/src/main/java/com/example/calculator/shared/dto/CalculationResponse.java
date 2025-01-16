package com.example.calculator.shared.dto;

import java.math.BigDecimal;

/*
CalculationResponse is a data transfer object (DTO) that represents a response to a calculation request.
It contains the result of the calculation, an error message if the calculation failed, and a request ID for correlation purposes.
*/

public class CalculationResponse {
    private BigDecimal result;
    private String error;
    
   
    private String requestId;

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


    public boolean hasError() {
        return error != null;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
} 