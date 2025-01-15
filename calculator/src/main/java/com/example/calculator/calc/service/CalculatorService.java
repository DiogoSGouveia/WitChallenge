package com.example.calculator.calc.service;

import com.example.calculator.shared.dto.CalculationRequest;
import com.example.calculator.shared.dto.CalculationResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

import java.math.RoundingMode;



@Service
public class CalculatorService {
    private final KafkaTemplate<String, CalculationResponse> kafkaTemplate;

    public CalculatorService(KafkaTemplate<String, CalculationResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    @KafkaListener(topics = "calculator-request-topic")
    public void handleCalculationRequest(CalculationRequest request) {
        System.out.println("Received request with correlationId: " + request.getCorrelationId());

        BigDecimal result = switch (request.getOperation()) {
            case "sum" -> request.getA().add(request.getB());
            case "subtraction" -> request.getA().subtract(request.getB());
            case "multiplication" -> request.getA().multiply(request.getB());
            case "division" -> request.getB().compareTo(BigDecimal.ZERO) != 0 
                ? request.getA().divide(request.getB(), 10, RoundingMode.HALF_UP) 
                : null;
            default -> null;
        };
        System.out.println("Calculated result: " + result);

        CalculationResponse response = new CalculationResponse(
            result, 
            result == null ? "Invalid operation or division by zero" : null
        );
        response.setCorrelationId(request.getCorrelationId());
        System.out.println("Sending response with correlationId: " + response.getCorrelationId());
        System.out.println("Sending response with error: " + response.getError());
        System.out.println("Sending response with result: " + response.getResult());
        String topic = response.hasError() ? "calculator-error-topic" : "calculator-response-topic";
        System.out.println("Sending response to " + topic + " for correlationId: " + response.getCorrelationId());
        kafkaTemplate.send(topic, response);
    }


}
