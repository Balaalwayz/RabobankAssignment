package nl.rabobank.exception.handler;

import nl.rabobank.exception.PowerOfAttorneyGenericException;
import nl.rabobank.exception.PowerOfAttorneyNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class POAExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(
                        Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setValidationErrors(fieldErrors);

        return errorResponse;
    }

    @ExceptionHandler(PowerOfAttorneyGenericException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlePowerOfAttorneyGenericException(
            PowerOfAttorneyGenericException ex) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(ex.getErrorMessage());

        return errorResponse;
    }


    @ExceptionHandler(PowerOfAttorneyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlePowerOfAttorneyNotFoundException(
            PowerOfAttorneyNotFoundException ex) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(ex.getErrorMessage());

        return errorResponse;
    }
}
