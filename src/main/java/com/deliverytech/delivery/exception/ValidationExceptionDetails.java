package com.deliverytech.delivery.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationExceptionDetails {
    private String field;
    private String message;
}