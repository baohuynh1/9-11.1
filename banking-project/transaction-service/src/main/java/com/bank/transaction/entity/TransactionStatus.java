package com.bank.transaction.entity;

public enum TransactionStatus {
    /** Đã kiểm tra hợp lệ (tài khoản tồn tại, đủ số dư), chờ hạch toán. */
    VALIDATED,
    /** Bị từ chối do không thỏa điều kiện nghiệp vụ. */
    REJECTED
}
