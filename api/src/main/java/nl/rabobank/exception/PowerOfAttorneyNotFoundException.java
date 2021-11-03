package nl.rabobank.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class PowerOfAttorneyNotFoundException extends Exception {
    String errorMessage;
}
