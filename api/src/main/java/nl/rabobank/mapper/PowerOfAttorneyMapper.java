package nl.rabobank.mapper;

import nl.rabobank.account.Account;
import nl.rabobank.account.PaymentAccount;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.dto.PowerOfAttorneyDTO;
import nl.rabobank.exception.PowerOfAttorneyGenericException;
import nl.rabobank.mongo.entity.PowerOfAttorneyEntity;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.function.Function;

public class PowerOfAttorneyMapper {

    Function<PowerOfAttorneyDTO, Account> dto_To_Domain_Account = dto -> {

        if ("SAVINGS".equals(dto.getAccountType().name()))
            return SavingsAccount.builder()
                    .accountNumber(dto.getAccountNumber())
                    .accountHolderName(dto.getGrantorName())
                    .balance(dto.getBalance())
                    .build();
        else
            return PaymentAccount.builder()
                    .accountNumber(dto.getAccountNumber())
                    .accountHolderName(dto.getGrantorName())
                    .balance(dto.getBalance())
                    .build();
    };

    public PowerOfAttorneyEntity toEntity(Optional<PowerOfAttorneyDTO> power_Of_Attorney_Dto) throws PowerOfAttorneyGenericException {
        PowerOfAttorneyEntity powerOfAttorneyEntityWrapper = new PowerOfAttorneyEntity();

        power_Of_Attorney_Dto.ifPresent(powerOfAttorneyDTO -> {
                    Assert.notNull(powerOfAttorneyDTO.getAccountNumber(), "AccountNumber cannot be null");
                    powerOfAttorneyEntityWrapper
                            .setPowerOfAttorney(PowerOfAttorney.builder()
                                    .granteeName(powerOfAttorneyDTO.getGranteeName())
                                    .grantorName(powerOfAttorneyDTO.getGrantorName())
                                    .account(dto_To_Domain_Account.apply(powerOfAttorneyDTO))
                                    .authorization(powerOfAttorneyDTO.getAuthorization())
                                    .build());
                    powerOfAttorneyEntityWrapper.setId(powerOfAttorneyDTO.getAccountNumber());
                }
        );
        return powerOfAttorneyEntityWrapper;
    }

}
