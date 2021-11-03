package nl.rabobank.mapper;

import nl.rabobank.constants.AccountType;
import nl.rabobank.dto.PowerOfAttorneyDTO;
import nl.rabobank.exception.PowerOfAttorneyGenericException;
import nl.rabobank.mongo.entity.PowerOfAttorneyEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PowerOfAttorneyMapperTest {

    PowerOfAttorneyMapper poaMapper = new PowerOfAttorneyMapper();

    @Test
    @DisplayName("test_entity_mapper_from_a_dto_with_savings_account_and_return_an_entity")
    void testDtoToEntityForASavingsAccount() throws PowerOfAttorneyGenericException {
        //Arrange
        PowerOfAttorneyDTO poaDTO = PowerOfAttorneyDTO.builder()
                .accountNumber("NL13456")
                .accountType(AccountType.SAVINGS)
                .granteeName("Paul")
                .grantorName("Victoria")
                .build();

        //Act
        PowerOfAttorneyEntity poaEntity = poaMapper.toEntity(Optional.of(poaDTO));

        //Assert
        assertNotNull(poaEntity);
        assertEquals("NL13456", poaEntity.getPowerOfAttorney().getAccount().getAccountNumber());
    }

    @Test
    @DisplayName("test_entity_mapper_from_a_dto_with_payment_account_and_return_an_entity")
    void testDtoToEntityForAPaymentAccount() throws PowerOfAttorneyGenericException {
        //Arrange
        PowerOfAttorneyDTO poaDTO = PowerOfAttorneyDTO.builder()
                .accountNumber("NL14456")
                .accountType(AccountType.PAYMENT)
                .granteeName("Paul")
                .grantorName("Victoria")
                .build();

        //Act
        PowerOfAttorneyEntity poaEntity = poaMapper.toEntity(Optional.of(poaDTO));

        //Assert
        assertNotNull(poaEntity);
        assertEquals("NL14456", poaEntity.getPowerOfAttorney().getAccount().getAccountNumber());
    }

    @Test
    @DisplayName("test_entity_mapper_from_an_empty_dto_and_returns_an_empty_entity")
    void testDtoToEntityForNullValue() throws PowerOfAttorneyGenericException {
        //Arrange

        //Act
        PowerOfAttorneyEntity entity = poaMapper.toEntity(Optional.empty());

        //Assert
        assertNotNull(entity);
    }

    @Test
    @DisplayName("test_entity_mapper_from_a_dto_with_null_account_number_and_throws_an_exception")
    void testDtoToEntityForNullAccountNumber() {
        //Arrange
        PowerOfAttorneyDTO dto = PowerOfAttorneyDTO.builder()
                .accountNumber(null)
                .accountType(AccountType.PAYMENT)
                .granteeName("Paul")
                .grantorName("Victoria")
                .build();

        //Act and Assert
        assertThrows(IllegalArgumentException.class, () -> poaMapper.toEntity(Optional.of(dto)));

    }
}