package com.example.calculator.rest.service;

import com.example.calculator.shared.dto.CalculationRequest;
import com.example.calculator.shared.dto.CalculationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import java.util.concurrent.CompletableFuture;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CalculatorService {
    private static final Logger logger = LoggerFactory.getLogger(CalculatorService.class);
    private final KafkaTemplate<String, CalculationRequest> kafkaTemplate;
    private final Map<String, CompletableFuture<CalculationResponse>> pendingResponses = new ConcurrentHashMap<>();

    public CalculatorService(KafkaTemplate<String, CalculationRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CalculationResponse calculate(CalculationRequest request) {
        try {
            String correlationId = request.getCorrelationId();
            logger.info("Processing calculation request with correlationId: {}", correlationId);
            logger.debug("Request details - Operation: {}, A: {}, B: {}", 
                request.getOperation(), request.getA(), request.getB());
            
            CompletableFuture<CalculationResponse> future = new CompletableFuture<>();
            pendingResponses.put(correlationId, future);

            kafkaTemplate.send("calculator-request-topic", request);
            logger.info("Request sent to calculator service with correlationId: {}", correlationId);

            CalculationResponse response = future.get(30, java.util.concurrent.TimeUnit.SECONDS);
            logger.info("Received response for correlationId: {}", correlationId);
            return response;
        } catch (Exception e) {
            logger.error("Error processing calculation request: {}", e.getMessage(), e);
            return new CalculationResponse(null, "Error: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "calculator-response-topic")
    public void handleCalculationResponse(CalculationResponse response) {
        String correlationId = response.getCorrelationId();
        logger.info("Received successful calculation response for correlationId: {}", correlationId);
        logger.debug("Response result: {}", response.getResult());
        
        CompletableFuture<CalculationResponse> future = pendingResponses.remove(correlationId);
        if (future != null) {
            future.complete(response);
        } else {
            logger.warn("No pending request found for correlationId: {}", correlationId);
        }
    }

    @KafkaListener(topics = "calculator-error-topic")
    public void handleCalculationError(CalculationResponse response) {
        String correlationId = response.getCorrelationId();
        logger.info("Received error response for correlationId: {}", correlationId);
        logger.debug("Error details: {}", response.getError());
        
        CompletableFuture<CalculationResponse> future = pendingResponses.remove(correlationId);
        if (future != null) {
            future.complete(response);
        } else {
            logger.warn("No pending request found for correlationId: {}", correlationId);
        }
    }
}