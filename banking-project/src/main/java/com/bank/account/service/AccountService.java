package com.bank.account.service;

import com.bank.account.dto.AccountResponse;
import com.bank.account.dto.CreateAccountRequest;
import com.bank.account.entity.Account;
import com.bank.account.exception.AccountNotFoundException;
import com.bank.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

@Service
public class AccountService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountResponse create(CreateAccountRequest req) {
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setOwnerName(req.ownerName().trim());
        account.setEmail(req.email().trim());
        account.setPhone(req.phone());
        account.setAccountType(req.accountType());
        account.setBalance(req.initialDeposit() != null ? req.initialDeposit() : BigDecimal.ZERO);
        account.setCurrency(req.currency() != null ? req.currency() : "VND");
        return AccountResponse.from(accountRepository.save(account));
    }

    @Transactional(readOnly = true)
    public AccountResponse getById(Long id) {
        return accountRepository.findById(id)
                .map(AccountResponse::from)
                .orElseThrow(() -> new AccountNotFoundException("Không tìm thấy tài khoản id = " + id));
    }

    @Transactional(readOnly = true)
    public AccountResponse getByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(AccountResponse::from)
                .orElseThrow(() -> new AccountNotFoundException("Không tìm thấy số tài khoản " + accountNumber));
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAll() {
        return accountRepository.findAll().stream().map(AccountResponse::from).toList();
    }

    /** Sinh số tài khoản 12 chữ số, đầu "9704", đảm bảo không trùng. */
    private String generateAccountNumber() {
        String number;
        do {
            number = "9704" + String.format("%08d", RANDOM.nextInt(100_000_000));
        } while (accountRepository.existsByAccountNumber(number));
        return number;
    }
}
