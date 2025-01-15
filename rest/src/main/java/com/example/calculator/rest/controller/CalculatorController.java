package com.example.calculator.rest.controller;

import com.example.calculator.rest.service.CalculatorService;
import com.example.calculator.rest.util.RequestTracker;
import com.example.calculator.shared.dto.CalculationRequest;
import com.example.calculator.shared.dto.CalculationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {
    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);
    private final CalculatorService calculatorService;
    private final RequestTracker requestTracker;

    public CalculatorController(CalculatorService calculatorService, RequestTracker requestTracker) {
        this.calculatorService = calculatorService;
        this.requestTracker = requestTracker;
    }

    @GetMapping("/{operation}")
    public ResponseEntity<CalculationResponse> calculate(
            @PathVariable String operation,
            @RequestParam BigDecimal a,
            @RequestParam BigDecimal b) {
        
        String requestId = requestTracker.getRequestId();
        logger.info("Processing calculation request with ID: {}", requestId);
        
        CalculationRequest request = new CalculationRequest(operation, a, b);
        request.setCorrelationId(requestId);
        
        CalculationResponse response = calculatorService.calculate(request);
        
        return ResponseEntity
            .ok()
            .header("Request-ID", requestId)
            .header("Correlation-ID", requestId)
            .body(response);
    }
}
