package com.example.calculator.rest.service;

import com.example.calculator.shared.dto.CalculationRequest;
import com.example.calculator.shared.dto.CalculationResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

@Service
public class CalculatorService {
    private final KafkaTemplate<String, CalculationRequest> kafkaTemplate;

    public CalculatorService(KafkaTemplate<String, CalculationRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CalculationResponse calculate(CalculationRequest request) {
        System.out.println("Sending request to calculator-request-topic: " + request);
        System.out.println("Sending operation: " + request.getOperation());
        System.out.println("Sending a: " + request.getA());
        System.out.println("Sending b: " + request.getB());
        try {
            kafkaTemplate.send("calculator-request-topic", request);
            System.out.println("Request sent successfully.");
            System.out.println("Waiting for response...");
            CalculationResponse response = handleCalculationResponse(null);
            System.out.println("Response: " + response);
            return response;
        } catch (Exception e) {
            return new CalculationResponse("Error: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "calculator-response-topic", groupId = "calculator-group")
    public CalculationResponse handleCalculationResponse(CalculationResponse response) {
        System.out.println("Received response: " + response);
        return response;
    }
}