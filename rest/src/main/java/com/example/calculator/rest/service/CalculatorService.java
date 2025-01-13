package com.example.calculator.rest.service;

import com.example.calculator.shared.dto.CalculationRequest;
import com.example.calculator.shared.dto.CalculationResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {
    private final KafkaTemplate<String, CalculationRequest> kafkaTemplate;

    public CalculatorService(KafkaTemplate<String, CalculationRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CalculationResponse calculate(CalculationRequest request) {
        try {
            kafkaTemplate.send("calculator-topic", request);
            return new CalculationResponse();
        } catch (Exception e) {
            return new CalculationResponse("Error: " + e.getMessage());
        }
    }
}