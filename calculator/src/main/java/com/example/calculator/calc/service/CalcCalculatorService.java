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

/*
CalcCalculatorService is a service that handles the calculation requests and responses for the calculator.
It uses Kafka to send and receive messages.
*/

@Service
public class CalcCalculatorService {
    private static final Logger logger = LoggerFactory.getLogger(CalcCalculatorService.class);
    private final KafkaTemplate<String, CalculationResponse> kafkaTemplate;

    public CalcCalculatorService(KafkaTemplate<String, CalculationResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    @KafkaListener(topics = "calculator-request-topic")
    public void handleCalculationRequest(CalculationRequest request) {
        try {
            MdcUtil.setRequestId(request.getRequestId());
            logger.info("Received calculation request with requestId: {}", request.getRequestId());
            
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
            response.setRequestId(request.getRequestId());

            String topic = response.hasError() ? "calculator-error-topic" : "calculator-response-topic";
            logger.info("Sending response to {} for requestId: {}", topic, response.getRequestId());

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
