package com.bank.transaction.exception;

public class AccountServiceUnavailableException extends RuntimeException {
    public AccountServiceUnavailableException(String message) {
        super(message);
    }
}
