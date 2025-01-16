package com.example.calculator.rest.controller;

import com.example.calculator.rest.service.RestCalculatorService;
import com.example.calculator.rest.util.RequestTracker;
import com.example.calculator.shared.dto.CalculationRequest;
import com.example.calculator.shared.dto.CalculationResponse;
import com.example.calculator.shared.util.MdcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;

/*
CalculatorController is a REST controller that provides a simple calculator API.
It handles GET requests to perform arithmetic operations.
*/

@RestController
@RequestMapping("/api/calculator")
public class RestCalculatorController {
    private static final Logger logger = LoggerFactory.getLogger(RestCalculatorController.class);
    private final RestCalculatorService calculatorService;
    private final RequestTracker requestTracker;

    public RestCalculatorController(RestCalculatorService calculatorService, RequestTracker requestTracker) {
        this.calculatorService = calculatorService;
        this.requestTracker = requestTracker;
    }

    @GetMapping("/{operation}")
    public ResponseEntity<CalculationResponse> calculate(
            @PathVariable String operation,
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b) {
        try {
            String requestId = requestTracker.getRequestId();
            MdcUtil.setRequestId(requestId);
            
            logger.info("Processing calculation request with ID: {}", requestId);
            
            CalculationRequest request = new CalculationRequest(operation, a, b);
            request.setRequestId(requestId);
            
            CalculationResponse response = calculatorService.calculate(request);

            
            return ResponseEntity
                .ok()
                .header("Request-ID", requestId)
               
                .body(response);
        } finally {
            MdcUtil.clearRequestId();
        }
    }
}
