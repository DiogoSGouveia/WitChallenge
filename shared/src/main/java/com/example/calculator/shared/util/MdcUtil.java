package com.example.calculator.shared.util;

import org.slf4j.MDC;

/*
MdcUtil is a utility class that provides methods with setters and getters for request IDs in the MDC (Mapped Diagnostic Context).
It is used to correlate requests and responses in the Kafka messaging system.
*/

public class MdcUtil {
    public static final String REQUEST_ID = "requestId";
    
    public static void setRequestId(String requestId) {
        if (requestId != null) {
            MDC.put(REQUEST_ID, requestId);
        }
    }
    
    public static String getRequestId() {
        return MDC.get(REQUEST_ID);
    }
    
    public static void clearRequestId() {
        MDC.remove(REQUEST_ID);
    }
}