package com.bank.transaction.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

/** Dữ liệu tài khoản nhận về từ account-service. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AccountDto(
        Long id,
        String accountNumber,
        String ownerName,
        BigDecimal balance,
        String currency,
        String status
) {
}
