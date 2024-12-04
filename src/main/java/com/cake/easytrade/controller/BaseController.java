package com.cake.easytrade.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    // Success Response
    protected <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok(body);
    }

    // Created Response
    protected <T> ResponseEntity<T> created(T body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // No Content Response
    protected ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }

    // Error Response
    protected <T> ResponseEntity<T> badRequest(T body) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    protected <T> ResponseEntity<T> notFound(T body) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    protected <T> ResponseEntity<T> internalServerError(T body) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}