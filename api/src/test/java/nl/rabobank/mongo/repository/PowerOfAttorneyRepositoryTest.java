package nl.rabobank.mongo.repository;

import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.mongo.entity.PowerOfAttorneyEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class PowerOfAttorneyRepositoryTest {

    @Autowired
    PowerOfAttorneyRepository powerOfAttorneyRepository;

    @BeforeEach
    void setUp() {
        PowerOfAttorneyEntity poaEntity = new PowerOfAttorneyEntity();
        poaEntity.setId("NL56RABO123456789");
        poaEntity.setPowerOfAttorney(PowerOfAttorney.builder()
                .grantorName("Hussain Bolt")
                .granteeName("Kasi Bennett")
                .build());
        powerOfAttorneyRepository.save(poaEntity);

    }

    @AfterEach
    void tearDown() {
        powerOfAttorneyRepository.deleteById("NL56RABO123456789");
        powerOfAttorneyRepository.deleteById("NL12RABO123456790");
        powerOfAttorneyRepository.deleteById("NL12RABO344556565");
        powerOfAttorneyRepository.deleteById("NL12IaBN3146789");
        powerOfAttorneyRepository.deleteById("NL24IaBN3146789");
    }

    @DisplayName("test_power_of_attorney_execution_for_success")
    @Test
    void testExecutePowerOfAttorney() {
        //Arrange
        PowerOfAttorneyEntity poaEntity = new PowerOfAttorneyEntity();
        poaEntity.setId("NL12RABO123456790");
        poaEntity.setPowerOfAttorney(PowerOfAttorney.builder()
                .grantorName("Paul")
                .granteeName("Victoria")
                .build());

        //Act
        PowerOfAttorneyEntity persistedPOAEntity = powerOfAttorneyRepository.save(poaEntity);

        //Assert
        assertNotNull(persistedPOAEntity.getId());
        assertEquals(poaEntity.getPowerOfAttorney().getGrantorName(),
                persistedPOAEntity.getPowerOfAttorney().getGrantorName());
        assertNotEquals(poaEntity.getPowerOfAttorney().getGranteeName(),
                persistedPOAEntity.getPowerOfAttorney().getGrantorName());


    }

    @DisplayName("test_power_of_attorney_inquiry_for_success")
    @Test
    void testInquirePowerOfAttorneyWithSuccess() {
        //Act
        List<PowerOfAttorneyEntity> poaList_of_paul = powerOfAttorneyRepository
                .findAllByPowerOfAttorneyGranteeName("Paul");

        List<PowerOfAttorneyEntity> poaList_of_kasi_bennett = powerOfAttorneyRepository
                .findAllByPowerOfAttorneyGranteeName("Kasi Bennett");

        //Assert
        assertEquals(0, poaList_of_paul.size());
        assertEquals(1, poaList_of_kasi_bennett.size());
    }

    @DisplayName("test_power_of_attorney_inquiry_for_failure")
    @ParameterizedTest
    @ValueSource(strings = {"Invalid User1", "Invalid User2"})
    void testInquirePowerOfAttorneyWithFailure(String granteeName) {
        //Act
        List<PowerOfAttorneyEntity> poaList_of_invalid_user = powerOfAttorneyRepository
                .findAllByPowerOfAttorneyGranteeName(granteeName);

        //Assert
        assertEquals(0, poaList_of_invalid_user.size());
    }

}