package nl.rabobank.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountType {

    SAVINGS("S"),
    PAYMENT("P");

    private String accountType;
}
