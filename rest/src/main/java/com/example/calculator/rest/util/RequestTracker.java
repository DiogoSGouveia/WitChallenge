package com.example.calculator.rest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/*
RequestTracker is a component that tracks the request ID for each request.
The primary purpose is to provide a unique identifier for each request in order to link requests and responses in the Kafka messaging system.
*/

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