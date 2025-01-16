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

/*
CalculatorService is a service that handles the calculation requests and responses for the rest api.
It uses Kafka to send and receive messages.
*/

@Service
public class RestCalculatorService {
    private static final Logger logger = LoggerFactory.getLogger(RestCalculatorService.class);
    private final KafkaTemplate<String, CalculationRequest> kafkaTemplate;
    private final Map<String, CompletableFuture<CalculationResponse>> pendingResponses = new ConcurrentHashMap<>();

    public RestCalculatorService(KafkaTemplate<String, CalculationRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CalculationResponse calculate(CalculationRequest request) {
        try {
            String requestId = request.getRequestId();
            logger.info("Processing calculation request with requestId: {}", requestId);
            logger.debug("Request details - Operation: {}, A: {}, B: {}", 
                request.getOperation(), request.getA(), request.getB());
            
            CompletableFuture<CalculationResponse> future = new CompletableFuture<>();
            pendingResponses.put(requestId, future);

            kafkaTemplate.send("calculator-request-topic", request);
            logger.info("Request sent to calculator service with requestId: {}", requestId);

            CalculationResponse response = future.get(30, java.util.concurrent.TimeUnit.SECONDS);
            logger.info("Received response for requestId: {}", requestId);
            return response;
        } catch (Exception e) {
            logger.error("Error processing calculation request: {}", e.getMessage(), e);
            return new CalculationResponse(null, "Error: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "calculator-response-topic")
    public void handleCalculationResponse(CalculationResponse response) {
        String requestId = response.getRequestId();
        logger.info("Received successful calculation response for requestId: {}", requestId);
        logger.debug("Response result: {}", response.getResult());
        
        CompletableFuture<CalculationResponse> future = pendingResponses.remove(requestId);
        if (future != null) {
            future.complete(response);
        } else {
            logger.warn("No pending request found for requestId: {}", requestId);
        }
    }

    @KafkaListener(topics = "calculator-error-topic")
    public void handleCalculationError(CalculationResponse response) {
        String requestId = response.getRequestId();
        logger.info("Received error response for requestId: {}", requestId);
        logger.debug("Error details: {}", response.getError());
        
        CompletableFuture<CalculationResponse> future = pendingResponses.remove(requestId);
        if (future != null) {
            future.complete(response);
        } else {
            logger.warn("No pending request found for requestId: {}", requestId);
        }
    }
}