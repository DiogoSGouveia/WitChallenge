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
    
    @KafkaListener(topics = "calculator-topic")
    public void handleCalculationRequest(CalculationRequest request) {
        BigDecimal result = switch (request.getOperation()) {
            case "sum" -> request.getA().add(request.getB());
            case "subtract" -> request.getA().subtract(request.getB());
            case "multiply" -> request.getA().multiply(request.getB());
            case "divide" -> request.getB().compareTo(BigDecimal.ZERO) != 0 
            ? request.getA().divide(request.getB(), 10, RoundingMode.HALF_UP) : null;  
            default -> null;
        };

        CalculationResponse response = new CalculationResponse(result, null);
        
        if (response.hasError()) {
            kafkaTemplate.send("calculator-error-topic", response);
        } else {
            kafkaTemplate.send("calculator-response-topic", response);
        }


    }


}
