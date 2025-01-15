package com.example.calculator.rest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class RequestTracker {
    private static final Logger logger = LoggerFactory.getLogger(RequestTracker.class);
    private final String requestId;

    public RequestTracker() {
        this.requestId = java.util.UUID.randomUUID().toString();
        logger.debug("Created new request tracker with ID: {}", requestId);
    }

    public String getRequestId() {
        return requestId;
    }
} 