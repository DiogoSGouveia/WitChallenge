package com.example.calculator.rest.controller;

import com.example.calculator.rest.service.CalculatorService;
import com.example.calculator.shared.dto.CalculationRequest;
import com.example.calculator.shared.dto.CalculationResponse;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("/{operation}")
    public CalculationResponse calculate(
            @PathVariable String operation,
            @RequestParam("a") BigDecimal a,
            @RequestParam("b") BigDecimal b) {
        CalculationRequest request = new CalculationRequest(operation, a, b);
        return calculatorService.calculate(request);
    }


}
