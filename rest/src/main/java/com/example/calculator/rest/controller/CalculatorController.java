package com.example.calculator.rest.controller;

import com.example.calculator.shared.dto.CalculationRequest;
import com.example.calculator.shared.dto.CalculationResponse;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.calculator.rest.service.CalculatorService;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("/{operation}")
    public ResponseEntity<CalculationResponse> calculate(@PathVariable String operation, @RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        CalculationRequest request = new CalculationRequest(operation, a, b);
        CalculationResponse response = calculatorService.calculate(request);
        return ResponseEntity.ok(response);
    }



}
