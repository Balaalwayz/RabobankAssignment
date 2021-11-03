package nl.rabobank.service;

import nl.rabobank.dto.PowerOfAttorneyDTO;
import nl.rabobank.exception.PowerOfAttorneyGenericException;
import nl.rabobank.exception.PowerOfAttorneyNotFoundException;
import nl.rabobank.mongo.entity.PowerOfAttorneyEntity;

import java.util.List;

public interface PowerOfAttorneyService {

    /**
     * Interface to inquire the Power Of Attorney for a particular user
     *
     * @param user
     * @return the list of PowerOfAttorney
     */
    List<PowerOfAttorneyEntity> inquirePowerOfAttorney(String user) throws PowerOfAttorneyNotFoundException;

    /**
     * Execute a power of attorney for an account
     *
     * @return the power of power for successful executions
     */
    PowerOfAttorneyEntity executePowerOfAttorney(PowerOfAttorneyDTO powerOfAttorneyDTOWrapper) throws PowerOfAttorneyGenericException;
}
