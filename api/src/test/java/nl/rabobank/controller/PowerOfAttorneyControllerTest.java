package nl.rabobank.controller;

import nl.rabobank.account.PaymentAccount;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.dto.PowerOfAttorneyDTO;
import nl.rabobank.exception.PowerOfAttorneyGenericException;
import nl.rabobank.exception.PowerOfAttorneyNotFoundException;
import nl.rabobank.mongo.entity.PowerOfAttorneyEntity;
import nl.rabobank.service.PowerOfAttorneyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PowerOfAttorneyControllerTest {

    @Mock
    PowerOfAttorneyService powerOfAttorneyServiceImpl;

    @InjectMocks
    PowerOfAttorneyController powerOfAttorneyController;

    @Test
    @DisplayName("test_inquire_power_of_attorney_and_returns_list_of_power_of_attorneys")
    void testInquirePowerOfAttorneyPerUserWithSuccess() throws PowerOfAttorneyNotFoundException {

        //Arrange
        when(powerOfAttorneyServiceImpl.inquirePowerOfAttorney(anyString()))
                .thenReturn(Stream.of(
                        getPowerOfAttorneyForSavingsAccount(),
                        getPowerOfAttorneyForPaymentAccount()
                ).collect(Collectors.toList()));

        //Act
        List<PowerOfAttorneyEntity> powerOfAttorneyOnAccountsList = powerOfAttorneyController.inquirePowerOfAttorneyPerGrantee("test");

        //Assert
        assertNotNull(powerOfAttorneyOnAccountsList, "Assert the list");
        assertEquals(2, powerOfAttorneyOnAccountsList.size());
        assertTrue(powerOfAttorneyOnAccountsList.get(0).getPowerOfAttorney().getAccount() instanceof SavingsAccount);
        assertTrue(powerOfAttorneyOnAccountsList.get(1).getPowerOfAttorney().getAccount() instanceof PaymentAccount);

        verify(powerOfAttorneyServiceImpl, times(1)).inquirePowerOfAttorney("test");
    }

    @Test
    @DisplayName("test_inquire_power_of_attorney_and_returns_empty_list")
    void testInquirePowerOfAttorneyPerUserWithEmptyResult() throws PowerOfAttorneyNotFoundException {

        //Arrange
        when(powerOfAttorneyServiceImpl.inquirePowerOfAttorney(anyString())).thenReturn(new ArrayList<>());

        //Act
        List<PowerOfAttorneyEntity> emptyList = powerOfAttorneyController.inquirePowerOfAttorneyPerGrantee("user_not_found");

        //Assert
        assertNotNull(emptyList, "Assert the list");
        assertTrue(emptyList.isEmpty());
    }

    @Test
    @DisplayName("test_execute_power_of_attorney_on_a_savings_account_and_returns_no_result_for_success")
    void testExecutePowerOfAttorneyOnSavingsAccount() throws PowerOfAttorneyGenericException {

        //Arrange
        PowerOfAttorneyDTO poaDTO = new PowerOfAttorneyDTO();
        poaDTO.setAccountNumber("NL12345");
        PowerOfAttorneyEntity poaWrapper = new PowerOfAttorneyEntity();
        poaWrapper.setId("1");

        when(powerOfAttorneyServiceImpl.executePowerOfAttorney(any(PowerOfAttorneyDTO.class)))
                .thenReturn(getPowerOfAttorneyForSavingsAccount());

        //Act
        powerOfAttorneyController
                .executePowerOfAttorneyOnSingleAccount(poaDTO);

        //Assert
        verify(powerOfAttorneyServiceImpl, times(1)).executePowerOfAttorney(poaDTO);
    }

    @Test
    @DisplayName("test_execute_power_of_attorney_on_a_payment_account_and_returns_no_result_for_success")
    void testExecutePowerOfAttorneyOnPaymentAccount() throws PowerOfAttorneyGenericException {

        //Arrange
        PowerOfAttorneyDTO poaDTO = new PowerOfAttorneyDTO();
        poaDTO.setAccountNumber("NL12345");
        PowerOfAttorneyEntity poaWrapper = new PowerOfAttorneyEntity();
        poaWrapper.setId("1");

        when(powerOfAttorneyServiceImpl.executePowerOfAttorney(any(PowerOfAttorneyDTO.class)))
                .thenReturn(getPowerOfAttorneyForPaymentAccount());

        //Act
        PowerOfAttorneyEntity persistedPowerOfAttorney = powerOfAttorneyController
                .executePowerOfAttorneyOnSingleAccount(poaDTO);

        //Assert
        assertNotNull(persistedPowerOfAttorney, "Assert the power of attorney execution");
        assertTrue(persistedPowerOfAttorney.getPowerOfAttorney().getAccount() instanceof PaymentAccount);

        verify(powerOfAttorneyServiceImpl, times(1)).executePowerOfAttorney(poaDTO);
    }


    private PowerOfAttorneyEntity getPowerOfAttorneyForSavingsAccount() {
        SavingsAccount savingsAccount = SavingsAccount.builder()
                .accountNumber("NL07RABO8441374295")
                .accountHolderName("Paul")
                .balance(500.0)
                .build();

        PowerOfAttorneyEntity entity = new PowerOfAttorneyEntity();
        entity.setId(savingsAccount.getAccountNumber());

        entity.setPowerOfAttorney(PowerOfAttorney.builder()
                .account(savingsAccount)
                .authorization(Authorization.WRITE)
                .granteeName("Victoria")
                .grantorName(savingsAccount.getAccountHolderName())
                .build());

        return entity;
    }

    private PowerOfAttorneyEntity getPowerOfAttorneyForPaymentAccount() {
        PaymentAccount paymentAccount = PaymentAccount.builder()
                .accountHolderName("Paul")
                .accountNumber("NL23RABO8441374300")
                .balance(250.9)
                .build();

        PowerOfAttorneyEntity entity = new PowerOfAttorneyEntity();
        entity.setId(paymentAccount.getAccountNumber());

        entity.setPowerOfAttorney(PowerOfAttorney.builder()
                .account(paymentAccount)
                .authorization(Authorization.READ)
                .granteeName("Victoria")
                .grantorName(paymentAccount.getAccountHolderName())
                .build());

        return entity;
    }

}