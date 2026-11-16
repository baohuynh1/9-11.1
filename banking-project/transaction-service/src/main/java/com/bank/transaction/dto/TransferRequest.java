package com.bank.transaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank(message = "fromAccountNumber không được để trống")
        String fromAccountNumber,

        @NotBlank(message = "toAccountNumber không được để trống")
        String toAccountNumber,

        @NotNull(message = "amount không được để trống")
        @DecimalMin(value = "1000", message = "amount tối thiểu 1000")
        BigDecimal amount,

        @Size(max = 255)
        String description
) {
}
