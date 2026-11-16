package com.bank.account.controller;

import com.bank.account.dto.AccountResponse;
import com.bank.account.dto.CreateAccountRequest;
import com.bank.account.service.AccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /** Tạo tài khoản mới → 201 Created */
    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {
        AccountResponse created = accountService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    /** Lấy danh sách tài khoản */
    @GetMapping
    public List<AccountResponse> getAll() {
        return accountService.getAll();
    }

    /** Truy vấn theo id */
    @GetMapping("/{id}")
    public AccountResponse getById(@PathVariable Long id) {
        return accountService.getById(id);
    }

    /** Truy vấn theo số tài khoản */
    @GetMapping("/number/{accountNumber}")
    public AccountResponse getByAccountNumber(@PathVariable String accountNumber,
                                              @RequestHeader(value = "X-Request-Id", required = false) String requestId) {
        log.info("[{}] >>> account-service nhan request kiem tra tai khoan {}", requestId, accountNumber);
        AccountResponse account = accountService.getByAccountNumber(accountNumber);
        log.info("[{}] <<< account-service tra ve tai khoan {} (chu TK: {})", requestId, accountNumber, account.ownerName());
        return account;
    }
}
