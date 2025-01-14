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

    @GetMapping("/sum")
    public ResponseEntity<CalculationResponse> calculate(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        System.out.println("Received a: " + a);
        System.out.println("Received b: " + b);
        System.out.println("Received operation: " + "sum");
        CalculationRequest request = new CalculationRequest("sum", a, b);
        CalculationResponse response = calculatorService.calculate(request);
        System.out.println("Response: " + response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subtraction")
    public ResponseEntity<Map<String, Integer>> subtract(@RequestParam int a, @RequestParam int b) {
        int result = a - b;
        Map<String, Integer> response = new HashMap<>();
        response.put("result", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/multiplication")
    public ResponseEntity<Map<String, Integer>> multiply(@RequestParam int a, @RequestParam int b) {
        int result = a * b;
        Map<String, Integer> response = new HashMap<>();
        response.put("result", result);
        return ResponseEntity.ok(response);
    }   

    @GetMapping("/division")
    public ResponseEntity<Map<String, Integer>> divide(@RequestParam int a, @RequestParam int b) {
        int result = a / b;
        Map<String, Integer> response = new HashMap<>();
        response.put("result", result);
        return ResponseEntity.ok(response);
    }


}
