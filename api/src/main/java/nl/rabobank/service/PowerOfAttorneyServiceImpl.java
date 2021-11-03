package nl.rabobank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.dto.PowerOfAttorneyDTO;
import nl.rabobank.exception.PowerOfAttorneyGenericException;
import nl.rabobank.exception.PowerOfAttorneyNotFoundException;
import nl.rabobank.mapper.PowerOfAttorneyMapper;
import nl.rabobank.mongo.entity.PowerOfAttorneyEntity;
import nl.rabobank.mongo.repository.PowerOfAttorneyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("powerOfAttorneyService")
@Slf4j
@RequiredArgsConstructor
public class PowerOfAttorneyServiceImpl implements PowerOfAttorneyService {


    final PowerOfAttorneyRepository powerOfAttorneyRepository;

    final PowerOfAttorneyMapper powerOfAttorneyMapper;

    @Override
    public List<PowerOfAttorneyEntity> inquirePowerOfAttorney(String user) throws PowerOfAttorneyNotFoundException {

        List<PowerOfAttorneyEntity> poaListOnAccounts = powerOfAttorneyRepository.findAllByPowerOfAttorneyGranteeName(user);

        if (poaListOnAccounts.isEmpty())
            throw new PowerOfAttorneyNotFoundException(String.format("No Power of Attorneys available for this user %s", user));
        return poaListOnAccounts;
    }

    @Override
    public PowerOfAttorneyEntity executePowerOfAttorney(PowerOfAttorneyDTO powerOfAttorneyDTO) throws PowerOfAttorneyGenericException {
        try {
            PowerOfAttorneyEntity power_Of_Attorney_entity = powerOfAttorneyMapper.toEntity(Optional.ofNullable(powerOfAttorneyDTO));

            return powerOfAttorneyRepository.save(power_Of_Attorney_entity);
        } catch (Exception exception) {
            log.error("Exception occured with executing PowerOfAttorney", exception);
            throw new PowerOfAttorneyGenericException("Exception occured with executing PowerOfAttorney", exception);
        }
    }
}
