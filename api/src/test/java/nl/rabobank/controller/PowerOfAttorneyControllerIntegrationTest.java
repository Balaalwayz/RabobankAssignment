package nl.rabobank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.constants.AccountType;
import nl.rabobank.dto.PowerOfAttorneyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PowerOfAttorneyControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    PowerOfAttorneyDTO request;

    @BeforeEach
    void setUp() {
        request = new PowerOfAttorneyDTO();
        request.setAccountNumber("NL12RABO344556565");
        request.setGranteeName("Paul");
        request.setGrantorName("Victoria");
        request.setBalance(333.0);
    }

    @Test
    @DisplayName("execute_write_access_poa_over_a_payment_account_on_endpoint_POST:/api/accounts/power-of-attorney/execute_and_expect_201_ok")
    void shouldReturn201OnExecuteOverAPaymentAccountWriteAccess() throws Exception {
        request.setAccountType(AccountType.PAYMENT);
        request.setAuthorization(Authorization.WRITE);

        mockMvc.perform(post("/api/accounts/power-of-attorney/execute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("execute_read_access_poa_over_a_payment_account_on_endpoint_POST:/api/accounts/power-of-attorney/execute_and_expect_201_ok")
    void shouldReturn201OnExecuteOverAPaymentAccountReadAccess() throws Exception {
        request.setAccountType(AccountType.PAYMENT);
        request.setAuthorization(Authorization.READ);

        mockMvc.perform(post("/api/accounts/power-of-attorney/execute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

    }


    @Test
    @DisplayName("execute_read_access_poa_over_a_savings_account_on_endpoint_POST:/api/accounts/power-of-attorney/execute_and_expect_201_ok")
    void shouldReturn201OnExecuteOverASavingsAccountReadAccess() throws Exception {
        request.setAccountType(AccountType.SAVINGS);
        request.setAuthorization(Authorization.READ);

        mockMvc.perform(post("/api/accounts/power-of-attorney/execute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("execute_write_access_poa_over_a_savings_account_on_endpoint_POST:/api/accounts/power-of-attorney/execute_and_expect_201_ok")
    void shouldReturn201OnExecuteOverASavingsAccountWriteAccess() throws Exception {
        request.setAccountType(AccountType.SAVINGS);
        request.setAuthorization(Authorization.WRITE);

        mockMvc.perform(post("/api/accounts/power-of-attorney/execute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("execute_read_access_poa_over_a_savings_account_on_endpoint_POST:/api/accounts/power-of-attorney/execute_and_expect_400_bad_request")
    void shouldReturn400OnExecuteOverASavingsAccountReadAccess() throws Exception {
        request.setAccountType(AccountType.SAVINGS);
        request.setAuthorization(Authorization.READ);

        request.setAccountNumber("INVALID_IBAN");

        mockMvc.perform(post("/api/accounts/power-of-attorney/execute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("inquire_all_power_of_attorney_granted_on_a_user_on_endpoint_GET:/api/accounts/power-of-attorney/inquire_and_expect_200_ok")
    void shouldReturn200OnInquiryForAGrantee() throws Exception {
        request.setAccountType(AccountType.SAVINGS);
        request.setAuthorization(Authorization.WRITE);

        mockMvc.perform(post("/api/accounts/power-of-attorney/execute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        request.setAccountType(AccountType.PAYMENT);
        request.setAuthorization(Authorization.WRITE);

        mockMvc.perform(post("/api/accounts/power-of-attorney/execute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/accounts/power-of-attorney/inquire")
                        .param("user", "Paul"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("NL12RABO344556565"));
    }


    @Test
    @DisplayName("inquire_power_of_attorney_granted_on_a_random_user_on_GET:/api/accounts/power-of-attorney/inquire_and_expect_404_not_found")
    void testInquirePowerOfAttorney() throws Exception {
        mockMvc.perform(get("/api/accounts/power-of-attorney/inquire")
                        .param("user", "test_user"))
                .andExpect(status().isNotFound());
    }
}
