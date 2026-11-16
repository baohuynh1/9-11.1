package com.bank.transaction.dto;

import com.bank.transaction.entity.Transaction;
import com.bank.transaction.entity.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        String requestId,
        String fromAccountNumber,
        String toAccountNumber,
        BigDecimal amount,
        String description,
        TransactionStatus status,
        String rejectReason,
        LocalDateTime createdAt
) {
    public static TransactionResponse from(Transaction t) {
        return new TransactionResponse(t.getId(), t.getRequestId(), t.getFromAccountNumber(),
                t.getToAccountNumber(), t.getAmount(), t.getDescription(), t.getStatus(),
                t.getRejectReason(), t.getCreatedAt());
    }
}
