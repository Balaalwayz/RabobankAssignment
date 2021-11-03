package nl.rabobank.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.dto.PowerOfAttorneyDTO;
import nl.rabobank.exception.PowerOfAttorneyGenericException;
import nl.rabobank.exception.PowerOfAttorneyNotFoundException;
import nl.rabobank.mongo.entity.PowerOfAttorneyEntity;
import nl.rabobank.service.PowerOfAttorneyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * This class exposes interfaces, that facilitates the following user actions
 * 1 - Inquire the Power of Attorney for a user
 * 2 - Execute the Power of Attoney for an account
 */
@RestController
@RequestMapping("/api/accounts/power-of-attorney")
@Slf4j
@RequiredArgsConstructor
public class PowerOfAttorneyController {

    final PowerOfAttorneyService powerOfAttorneyService;

    /**
     * This interface helps in inquiring all PowerOfAttorneys per Grantee/User
     *
     * @return the list of power of attorney
     */
    @GetMapping("/inquire")
    public List<PowerOfAttorneyEntity> inquirePowerOfAttorneyPerGrantee(@RequestParam final String user) throws PowerOfAttorneyNotFoundException {
        log.info("Inquire the Power of Attorney for user {}", user);
        return powerOfAttorneyService.inquirePowerOfAttorney(user);
    }

    /**
     * This interface helps in executing the power of attorney over a savings or payment account
     *
     * @param powerOfAttorneyRequest
     * @return
     */
    @PostMapping("/execute")
    @ResponseStatus(value = HttpStatus.CREATED)
    public PowerOfAttorneyEntity executePowerOfAttorneyOnSingleAccount(
            @Valid @RequestBody
                    final PowerOfAttorneyDTO powerOfAttorneyRequest) throws PowerOfAttorneyGenericException {
        log.info("Execute the power of attorney with request {}", powerOfAttorneyRequest);
        return powerOfAttorneyService.executePowerOfAttorney(powerOfAttorneyRequest);
    }
}
