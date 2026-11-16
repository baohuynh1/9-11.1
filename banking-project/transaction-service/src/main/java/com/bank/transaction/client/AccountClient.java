package com.bank.transaction.client;

import com.bank.transaction.exception.AccountNotFoundException;
import com.bank.transaction.exception.AccountServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Gọi HTTP nội bộ sang account-service qua tên container trong banking-network.
 */
@Component
public class AccountClient {

    private static final Logger log = LoggerFactory.getLogger(AccountClient.class);

    private final RestTemplate accountRestTemplate;
    private final String baseUrl;

    public AccountClient(RestTemplate accountRestTemplate,
                         @Value("${account-service.base-url}") String baseUrl) {
        this.accountRestTemplate = accountRestTemplate;
        this.baseUrl = baseUrl;
    }

    public AccountDto getAccountByNumber(String accountNumber, String requestId) {
        String path = "/api/accounts/number/{accountNumber}";
        log.info("[{}] >>> Goi account-service: GET {}{}", requestId, baseUrl,
                path.replace("{accountNumber}", accountNumber));

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);

        try {
            ResponseEntity<AccountDto> response = accountRestTemplate.exchange(
                    path, HttpMethod.GET, new HttpEntity<>(headers), AccountDto.class, accountNumber);
            AccountDto account = response.getBody();
            log.info("[{}] <<< account-service tra ve {}: tai khoan {} - chu TK: {}, so du: {} {}",
                    requestId, response.getStatusCode().value(), accountNumber,
                    account.ownerName(), account.balance(), account.currency());
            return account;
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("[{}] <<< account-service tra ve 404: tai khoan {} khong ton tai", requestId, accountNumber);
            throw new AccountNotFoundException("Tài khoản " + accountNumber + " không tồn tại");
        } catch (ResourceAccessException e) {
            log.error("[{}] !!! Khong ket noi duoc account-service tai {}: {}", requestId, baseUrl, e.getMessage());
            throw new AccountServiceUnavailableException("account-service hiện không phản hồi");
        }
    }
}
