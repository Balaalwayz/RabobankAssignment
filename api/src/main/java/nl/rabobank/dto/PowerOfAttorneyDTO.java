package nl.rabobank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.constants.AccountType;
import nl.rabobank.validator.AuthorizationTypeSubset;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PowerOfAttorneyDTO {

    private Double balance;

    @NotNull(message = "Grantee Name cannot be null")
    private String granteeName;

    @NotNull(message = "Grantor Name cannot be null")
    private String grantorName;

    @NotNull(message = "Account Number cannot be null")
    @Pattern(regexp = "[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}",
            message = "Provided Account number is not a valid IBAN")
    private String accountNumber;

    @NotNull(message = "AccountType cannot be null")
    private AccountType accountType;

    @NotNull(message = "Grantee Name cannot be null")
    @AuthorizationTypeSubset(anyOf = {Authorization.READ, Authorization.WRITE})
    private Authorization authorization;
}
