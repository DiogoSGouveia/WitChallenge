package com.example.calculator.calc.service;

import com.example.calculator.shared.dto.CalculationRequest;
import com.example.calculator.shared.dto.CalculationResponse;
import com.example.calculator.shared.util.MdcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculatorService {
    private static final Logger logger = LoggerFactory.getLogger(CalculatorService.class);
    private final KafkaTemplate<String, CalculationResponse> kafkaTemplate;

    public CalculatorService(KafkaTemplate<String, CalculationResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    @KafkaListener(topics = "calculator-request-topic")
    public void handleCalculationRequest(CalculationRequest request) {
        try {
            MdcUtil.setRequestId(request.getCorrelationId());
            logger.info("Received calculation request with correlationId: {}", request.getCorrelationId());
            
            BigDecimal result = switch (request.getOperation()) {
                case "sum" -> request.getA().add(request.getB());
                case "subtraction" -> request.getA().subtract(request.getB());
                case "multiplication" -> request.getA().multiply(request.getB());
                case "division" -> request.getB().compareTo(BigDecimal.ZERO) != 0 
                    ? request.getA().divide(request.getB(), 10, RoundingMode.HALF_UP) 
                    : null;
                default -> null;
            };
            
            logger.info("Calculated result: {}", result);

            CalculationResponse response = new CalculationResponse(
                result, 
                result == null ? "Invalid operation or division by zero" : null
            );
            response.setCorrelationId(request.getCorrelationId());

            String topic = response.hasError() ? "calculator-error-topic" : "calculator-response-topic";
            logger.info("Sending response to {} for correlationId: {}", topic, response.getCorrelationId());

            try {
                kafkaTemplate.send(topic, response);
                logger.info("Response sent successfully");
            } catch (Exception e) {
                logger.error("Error sending response: {}", e.getMessage(), e);
            }
        } finally {
            MdcUtil.clearRequestId();
        }
    }
}
