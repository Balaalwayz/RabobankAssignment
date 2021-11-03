package nl.rabobank.service;

import nl.rabobank.account.PaymentAccount;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.constants.AccountType;
import nl.rabobank.dto.PowerOfAttorneyDTO;
import nl.rabobank.exception.PowerOfAttorneyGenericException;
import nl.rabobank.exception.PowerOfAttorneyNotFoundException;
import nl.rabobank.mapper.PowerOfAttorneyMapper;
import nl.rabobank.mongo.entity.PowerOfAttorneyEntity;
import nl.rabobank.mongo.repository.PowerOfAttorneyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PowerOfAttorneyServiceImplTest {

    @Spy
    PowerOfAttorneyMapper entityMapper = new PowerOfAttorneyMapper();

    @Mock
    PowerOfAttorneyRepository powerOfAttorneyRepository;

    @InjectMocks
    PowerOfAttorneyServiceImpl powerOfAttorneyService;

    PowerOfAttorneyEntity entity = new PowerOfAttorneyEntity();

    @BeforeEach
    void setUp() {
        entity.setId("NL45RABO1236549977");
        entity.setPowerOfAttorney(PowerOfAttorney.builder()
                .granteeName("Kasi Bennett")
                .grantorName("Hussain Bolt")
                .authorization(Authorization.WRITE)
                .account(PaymentAccount.builder()
                        .accountNumber("NL45RABO1236549977")
                        .accountHolderName("Hussain Bolt")
                        .balance(400.0)
                        .build())
                .build());
    }

    @DisplayName("test_power_of_attorney_inquiry_returns_list_of_power_of_attorneys")
    @Test
    void test_powerofattorney_inquiy_return_list_of_records() throws PowerOfAttorneyNotFoundException {
        //Arrange
        String mockUser = "Kasi Bennett";
        when(powerOfAttorneyRepository.findAllByPowerOfAttorneyGranteeName(anyString()))
                .thenReturn(Stream.of(entity).collect(Collectors.toList()));

        //Act
        List<PowerOfAttorneyEntity> listOfPowerOfAttorney = powerOfAttorneyService.inquirePowerOfAttorney(mockUser);

        //Assert
        assertNotNull(listOfPowerOfAttorney);
        assertEquals(1, listOfPowerOfAttorney.size());
        verify(powerOfAttorneyRepository, times(1)).findAllByPowerOfAttorneyGranteeName(mockUser);

    }

    @DisplayName("test_power_of_attorney_inquiry_returns_empty_list_of_power_of_attorney")
    @Test
    void testPowerOfAttorneyInquiyReturnsEmptyList() {
        //Arrange
        when(powerOfAttorneyRepository.findAllByPowerOfAttorneyGranteeName(anyString()))
                .thenReturn(Collections.emptyList());

        //Act and Assert
        assertThrows(PowerOfAttorneyNotFoundException.class, () -> powerOfAttorneyService.inquirePowerOfAttorney("Kasi Bennett"));

    }

    @DisplayName("test_power_of_attorney_execution_returns_persisted_power_of_attorney")
    @ParameterizedTest
    @ValueSource(strings = {"NL45RABO987564456", "NL45RABO987564466", "NL45RABO987564470"})
    void testExecutePowerOfAttorneyServiceWithSuccess(String accountNumber) throws PowerOfAttorneyGenericException {
        //Arrange
        entity.setId(accountNumber);
        when(powerOfAttorneyRepository.save(any(PowerOfAttorneyEntity.class))).thenReturn(entity);
        PowerOfAttorneyDTO powerOfAttorneyDTO = new PowerOfAttorneyDTO();
        powerOfAttorneyDTO.setAccountNumber(accountNumber);
        powerOfAttorneyDTO.setAccountType(AccountType.SAVINGS);

        //Act
        PowerOfAttorneyEntity powerOfAttorney = powerOfAttorneyService.executePowerOfAttorney(powerOfAttorneyDTO);

        //Assert
        assertEquals(accountNumber, powerOfAttorney.getId());
        verify(powerOfAttorneyRepository, times(1)).save(any(PowerOfAttorneyEntity.class));

    }

    @DisplayName("test_power_of_attorney_execution_when_no_account_number_throws_exception")
    @Test
    void testExecutePowerOfAttorneyServiceWithNoAccountNumber() {

        //Act and Assert
        assertThrows(PowerOfAttorneyGenericException.class, () -> powerOfAttorneyService.executePowerOfAttorney(new PowerOfAttorneyDTO()));
        verify(powerOfAttorneyRepository, times(0)).save(any(PowerOfAttorneyEntity.class));

    }
}