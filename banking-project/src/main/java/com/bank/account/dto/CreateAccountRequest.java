package com.bank.account.dto;

import com.bank.account.entity.AccountType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateAccountRequest(
        @NotBlank(message = "ownerName không được để trống")
        @Size(max = 100)
        String ownerName,

        @NotBlank(message = "email không được để trống")
        @Email(message = "email không hợp lệ")
        String email,

        @Pattern(regexp = "^[0-9+]{9,15}$", message = "phone không hợp lệ")
        String phone,

        @NotNull(message = "accountType phải là SAVINGS hoặc CHECKING")
        AccountType accountType,

        @DecimalMin(value = "0", message = "initialDeposit không được âm")
        BigDecimal initialDeposit,

        @Pattern(regexp = "^[A-Z]{3}$", message = "currency gồm 3 chữ in hoa, VD: VND")
        String currency
) {
}
