package com.example.calculator.rest.controller;

import com.example.calculator.shared.dto.CalculationRequest;
import com.example.calculator.shared.dto.CalculationResponse;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {


    @GetMapping("/sum")
    public ResponseEntity<Map<String, Integer>> sum(@RequestParam int a, @RequestParam int b) {
        int result = a + b;
        Map<String, Integer> response = new HashMap<>();
        response.put("result", result);
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
