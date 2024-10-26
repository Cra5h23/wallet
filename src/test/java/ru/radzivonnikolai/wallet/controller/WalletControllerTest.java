package ru.radzivonnikolai.wallet.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import ru.radzivonnikolai.wallet.model.Wallet;
import ru.radzivonnikolai.wallet.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikolai Radzivon
 * @Date 26.10.2024
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class WalletControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int localServerPort;

    @Autowired
    private WalletRepository repository;

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        this.wallet = repository.save(new Wallet());
    }

    @Test
    public void updateBalanceTestDepositPlus100() {
        var id = wallet.getId();
        var dto = """
                {
                    "walletId": "%s",
                    "operationType": "DEPOSIT",
                    "amount": 100
                }""".formatted(id);
        var httpHeaders = new HttpHeaders();

        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ResponseEntity<String> postResponse = testRestTemplate.exchange(
                "http://localhost:" + localServerPort + "/api/v1/wallet",
                HttpMethod.POST,
                new HttpEntity<>(dto, httpHeaders),
                String.class
        );

        assertNotNull(postResponse);
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, postResponse.getHeaders().getContentType());
        assertEquals("На баланс кошелька с id: %s добавлено 100. Текущий баланс 100".formatted(id),
                postResponse.getBody());
    }

    @Test
    public void updateBalanceTestDepositMinus100() {
        var id = wallet.getId();
        var dto = """
                {
                    "walletId": "%s",
                    "operationType": "DEPOSIT",
                    "amount": -100
                }""".formatted(id);
        var httpHeaders = new HttpHeaders();

        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ResponseEntity<String> postResponse = testRestTemplate.exchange(
                "http://localhost:" + localServerPort + "/api/v1/wallet",
                HttpMethod.POST,
                new HttpEntity<>(dto, httpHeaders),
                String.class
        );

        assertNotNull(postResponse);
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, postResponse.getHeaders().getContentType());
        assertNotNull(postResponse.getBody());
    }

    @Test
    public void updateBalanceTestWithdrawMinus100() {
        var id = wallet.getId();
        wallet.setAmount(new BigDecimal(1000));
        repository.save(wallet);

        var dto = """
                {
                    "walletId": "%s",
                    "operationType": "WITHDRAW",
                    "amount": 100
                }""".formatted(id);
        var httpHeaders = new HttpHeaders();

        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ResponseEntity<String> postResponse = testRestTemplate.exchange(
                "http://localhost:" + localServerPort + "/api/v1/wallet",
                HttpMethod.POST,
                new HttpEntity<>(dto, httpHeaders),
                String.class
        );

        assertNotNull(postResponse);
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, postResponse.getHeaders().getContentType());
        assertEquals("С баланса кошелька с id: %s снято 100. Текущий баланс 900".formatted(id),
                postResponse.getBody());

        Optional<Wallet> byId = repository.findById(id);

        byId.ifPresent(w -> assertEquals(900, w.getAmount().intValue()));
    }

    @Test
    public void updateBalanceTestWalletIdNotExists() {
        var id = wallet.getId();
        var dto = """
                {
                    "operationType": "DEPOSIT",
                    "amount": 100
                }""";
        var httpHeaders = new HttpHeaders();

        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ResponseEntity<String> postResponse = testRestTemplate.exchange(
                "http://localhost:" + localServerPort + "/api/v1/wallet",
                HttpMethod.POST,
                new HttpEntity<>(dto, httpHeaders),
                String.class
        );

        assertNotNull(postResponse);
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, postResponse.getHeaders().getContentType());
        assertNotNull(postResponse.getBody());
    }

    @Test
    public void updateBalanceTestOperationTypeNotExists() {
        var id = wallet.getId();
        var dto = """
                {
                    "walletId": "%s",
                    "operationType": "DEPOSIT"
                }""".formatted(id);
        var httpHeaders = new HttpHeaders();

        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ResponseEntity<String> postResponse = testRestTemplate.exchange(
                "http://localhost:" + localServerPort + "/api/v1/wallet",
                HttpMethod.POST,
                new HttpEntity<>(dto, httpHeaders),
                String.class
        );

        assertNotNull(postResponse);
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, postResponse.getHeaders().getContentType());
        assertNotNull(postResponse.getBody());
    }

    @Test
    public void updateBalanceTestAmountNotExists() {
        var id = wallet.getId();
        var dto = """
                {
                    "walletId": "%s",
                    "amount": 100
                }""".formatted(id);
        var httpHeaders = new HttpHeaders();

        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ResponseEntity<String> postResponse = testRestTemplate.exchange(
                "http://localhost:" + localServerPort + "/api/v1/wallet",
                HttpMethod.POST,
                new HttpEntity<>(dto, httpHeaders),
                String.class
        );

        assertNotNull(postResponse);
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, postResponse.getHeaders().getContentType());
        assertNotNull(postResponse.getBody());
    }


    @Test
    void createNewWalletTest() {
        var postResponse = testRestTemplate.exchange(
                "http://localhost:" + localServerPort + "/api/v1/new-wallet",
                HttpMethod.POST,
                new HttpEntity<>(null),
                Object.class
        );

        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());

        var createdWallet = (String) postResponse.getBody();
        assertNotNull(createdWallet);

        var uuid = UUID.fromString(createdWallet.substring(createdWallet.lastIndexOf("/") + 1));

        Optional<Wallet> byId = repository.findById(uuid);

        byId.ifPresent(w -> assertAll(
                () -> assertEquals(BigDecimal.ZERO, w.getAmount()),
                () -> assertEquals(uuid, w.getId())
        ));
    }

    @Test
    public void getWalletTestValid() {
        var walletId = wallet.getId();

        ResponseEntity<Object> getResponse = testRestTemplate.getForEntity(
                "http://localhost:" + localServerPort + "/api/v1/wallets/{WALLET_UUID}",
                Object.class,
                walletId
        );
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        var body = (LinkedHashMap) getResponse.getBody();

        assertNotNull(body);
        assertEquals(walletId.toString(), body.get("walletId"));
        assertEquals(0, body.get("amount"));
    }

    @Test
    public void getWalletTestWalletIdNotExists() {
        var walletId = UUID.randomUUID();

        ResponseEntity<Object> getResponse = testRestTemplate.getForEntity(
                "http://localhost:" + localServerPort + "/api/v1/wallets/{WALLET_UUID}",
                Object.class,
                walletId
        );
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
        var body = (LinkedHashMap) getResponse.getBody();

        assertNotNull(body);
        assertEquals("Not Found", body.get("reason"));
        assertEquals("NOT_FOUND", body.get("status"));
        assertEquals("Кошелёк с id: %s не найден".formatted(walletId), body.get("message"));
        assertNotNull(body.get("timestamp"));
        assertEquals("/api/v1/wallets/%s".formatted(walletId), body.get("path"));
    }

    @Test
    public void getWalletTestWalletId1() {
        ResponseEntity<Object> getResponse = testRestTemplate.getForEntity(
                "http://localhost:" + localServerPort + "/api/v1/wallets/{WALLET_UUID}",
                Object.class,
                "1"
        );
        assertEquals(HttpStatus.BAD_REQUEST, getResponse.getStatusCode());
        var body = (LinkedHashMap) getResponse.getBody();

        assertNotNull(body);
        assertEquals("Bad Request", body.get("reason"));
        assertEquals("BAD_REQUEST", body.get("status"));
        assertEquals("Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; Invalid UUID string: 1", body.get("message"));
        assertNotNull(body.get("timestamp"));
        assertEquals("/api/v1/wallets/1", body.get("path"));
    }
}