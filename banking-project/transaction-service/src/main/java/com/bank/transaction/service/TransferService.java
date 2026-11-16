package com.bank.transaction.service;

import com.bank.transaction.client.AccountClient;
import com.bank.transaction.client.AccountDto;
import com.bank.transaction.dto.TransactionResponse;
import com.bank.transaction.dto.TransferRequest;
import com.bank.transaction.entity.Transaction;
import com.bank.transaction.entity.TransactionStatus;
import com.bank.transaction.exception.AccountNotFoundException;
import com.bank.transaction.exception.BusinessRuleException;
import com.bank.transaction.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TransferService {

    private static final Logger log = LoggerFactory.getLogger(TransferService.class);

    private final AccountClient accountClient;
    private final TransactionRepository transactionRepository;

    public TransferService(AccountClient accountClient, TransactionRepository transactionRepository) {
        this.accountClient = accountClient;
        this.transactionRepository = transactionRepository;
    }

    public TransactionResponse transfer(TransferRequest req) {
        String requestId = UUID.randomUUID().toString();
        log.info("[{}] Nhan yeu cau chuyen tien {} -> {}, so tien {}",
                requestId, req.fromAccountNumber(), req.toAccountNumber(), req.amount());

        if (req.fromAccountNumber().equals(req.toAccountNumber())) {
            throw reject(requestId, req, "Tài khoản nguồn và đích không được trùng nhau");
        }

        // 1. Kiểm tra tài khoản NGUỒN qua account-service (bắt buộc)
        AccountDto source;
        try {
            source = accountClient.getAccountByNumber(req.fromAccountNumber(), requestId);
        } catch (AccountNotFoundException e) {
            saveRejected(requestId, req, "Tài khoản nguồn không tồn tại");
            throw new AccountNotFoundException("Tài khoản nguồn " + req.fromAccountNumber() + " không tồn tại");
        }

        if (!"ACTIVE".equals(source.status())) {
            throw reject(requestId, req, "Tài khoản nguồn không ở trạng thái ACTIVE");
        }
        if (source.balance() == null || source.balance().compareTo(req.amount()) < 0) {
            throw reject(requestId, req, "Số dư tài khoản nguồn không đủ");
        }

        // 2. Kiểm tra tài khoản ĐÍCH
        try {
            accountClient.getAccountByNumber(req.toAccountNumber(), requestId);
        } catch (AccountNotFoundException e) {
            saveRejected(requestId, req, "Tài khoản đích không tồn tại");
            throw new AccountNotFoundException("Tài khoản đích " + req.toAccountNumber() + " không tồn tại");
        }

        // 3. Hợp lệ -> lưu giao dịch
        Transaction tx = build(requestId, req, TransactionStatus.VALIDATED, null);
        Transaction saved = transactionRepository.save(tx);
        log.info("[{}] Giao dich #{} hop le, da luu voi trang thai {}", requestId, saved.getId(), saved.getStatus());
        return TransactionResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getAll() {
        return transactionRepository.findAll().stream().map(TransactionResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public TransactionResponse getById(Long id) {
        return transactionRepository.findById(id)
                .map(TransactionResponse::from)
                .orElseThrow(() -> new AccountNotFoundException("Không tìm thấy giao dịch id = " + id));
    }

    private BusinessRuleException reject(String requestId, TransferRequest req, String reason) {
        saveRejected(requestId, req, reason);
        return new BusinessRuleException(reason);
    }

    private void saveRejected(String requestId, TransferRequest req, String reason) {
        transactionRepository.save(build(requestId, req, TransactionStatus.REJECTED, reason));
        log.warn("[{}] Tu choi giao dich: {}", requestId, reason);
    }

    private Transaction build(String requestId, TransferRequest req, TransactionStatus status, String reason) {
        Transaction tx = new Transaction();
        tx.setRequestId(requestId);
        tx.setFromAccountNumber(req.fromAccountNumber());
        tx.setToAccountNumber(req.toAccountNumber());
        tx.setAmount(req.amount());
        tx.setDescription(req.description());
        tx.setStatus(status);
        tx.setRejectReason(reason);
        return tx;
    }
}
