package com.bank.transaction.controller;

import com.bank.transaction.dto.TransactionResponse;
import com.bank.transaction.dto.TransferRequest;
import com.bank.transaction.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransferService transferService;

    public TransactionController(TransferService transferService) {
        this.transferService = transferService;
    }

    /** Chuyển tiền: kiểm tra tài khoản qua account-service rồi lưu giao dịch. */
    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse transfer(@Valid @RequestBody TransferRequest request) {
        return transferService.transfer(request);
    }

    @GetMapping
    public List<TransactionResponse> getAll() {
        return transferService.getAll();
    }

    @GetMapping("/{id}")
    public TransactionResponse getById(@PathVariable Long id) {
        return transferService.getById(id);
    }
}
