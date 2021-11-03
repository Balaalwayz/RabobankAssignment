package nl.rabobank.exception;

import lombok.Getter;


@Getter
public class PowerOfAttorneyGenericException extends Exception {
    String errorMessage;
    Throwable exception;

    public PowerOfAttorneyGenericException(String errorMessage, Throwable exception) {
        super(errorMessage, exception);
        this.errorMessage = errorMessage;
        this.exception = exception;
    }
}
