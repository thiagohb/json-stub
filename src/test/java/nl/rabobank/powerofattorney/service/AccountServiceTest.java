package nl.rabobank.powerofattorney.service;

import nl.rabobank.powerofattorney.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountServiceTest {

    private static final String SERVICE_URL = "http://rbservice.tst.nl:654/accs/";
    private static final String ACCOUNT_PREFFIX = "NL23RABO";

    private AccountService service;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        service = new AccountServiceImpl(restTemplate);
        ReflectionTestUtils.setField(service, "url", SERVICE_URL);
    }

    @Test
    void testGetAccount() {
        // given
        final String accountId = "123456789";
        final long balance = 50L;
        final String owner = "Levi";
        final String created = "12-10-2007";
        final String ended = "13-06-2009";

        final Account account = new Account(ACCOUNT_PREFFIX + accountId, owner, balance, created);
        account.setEnded(ended);
        when(restTemplate.getForObject(SERVICE_URL + accountId, Account.class)).thenReturn(account);

        // when
        final Account result = service.getAccount(ACCOUNT_PREFFIX + accountId);

        // then
        assertNotNull(result);
        assertEquals(ACCOUNT_PREFFIX + accountId, result.getId());
        assertEquals(balance, result.getBalance());
        assertEquals(owner, result.getOwner());
        assertEquals(created, result.getCreated());
        assertEquals(ended, result.getEnded());
    }

}
