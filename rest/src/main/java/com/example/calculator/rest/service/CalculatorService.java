package com.example.calculator.rest.service;

import com.example.calculator.shared.dto.CalculationRequest;
import com.example.calculator.shared.dto.CalculationResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import java.util.concurrent.CompletableFuture;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

@Service
public class CalculatorService {
    private final KafkaTemplate<String, CalculationRequest> kafkaTemplate;
    private final Map<String, CompletableFuture<CalculationResponse>> pendingResponses = new ConcurrentHashMap<>();

    public CalculatorService(KafkaTemplate<String, CalculationRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CalculationResponse calculate(CalculationRequest request) {
        try {
            // Generate a unique correlation ID
            String correlationId = UUID.randomUUID().toString();
            request.setCorrelationId(correlationId);
            
            // Create a CompletableFuture to wait for the response
            CompletableFuture<CalculationResponse> future = new CompletableFuture<>();
            pendingResponses.put(correlationId, future);

            System.out.println("Sending request with correlationId: " + correlationId);
            kafkaTemplate.send("calculator-request-topic", request);

            // Wait for the response with a timeout
            return future.get(30, java.util.concurrent.TimeUnit.SECONDS);
        } catch (Exception e) {
            return new CalculationResponse(null, "Error: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "calculator-response-topic")
    public void handleCalculationResponse(CalculationResponse response) {
        System.out.println("Received response for correlationId: " + response.getCorrelationId());
        CompletableFuture<CalculationResponse> future = pendingResponses.remove(response.getCorrelationId());
        if (future != null) {
            future.complete(response);
        }
    }

    @KafkaListener(topics = "calculator-error-topic")
    public void handleCalculationError(CalculationResponse response) {
        System.out.println("Received error for correlationId: " + response.getCorrelationId());
        CompletableFuture<CalculationResponse> future = pendingResponses.remove(response.getCorrelationId());
        if (future != null) {
            future.complete(response);
        }
    }
}