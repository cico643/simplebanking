package com.cico643.simplebanking.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

public class ApiError {
    @JsonProperty("success")
    private boolean success = false;
    @JsonProperty("status")
    private HttpStatus status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private Map<String, Object> data = Collections.emptyMap();

    @JsonCreator
    public ApiError(HttpStatus status) {
        this.status = status;
        this.message = "Unexpected error";
    }

    @JsonCreator
    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
    @JsonCreator
    public ApiError(HttpStatus status, String message, Map<String, Object> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "success=" + success +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
