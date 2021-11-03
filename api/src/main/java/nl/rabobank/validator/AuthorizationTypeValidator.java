package nl.rabobank.validator;

import nl.rabobank.authorizations.Authorization;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class AuthorizationTypeValidator implements ConstraintValidator<AuthorizationTypeSubset, Authorization> {
    private Authorization[] authorizationSet;

    @Override
    public void initialize(AuthorizationTypeSubset constraintAnnotation) {
        this.authorizationSet = constraintAnnotation.anyOf();
    }

    @Override
    public boolean isValid(Authorization authorization, ConstraintValidatorContext constraintValidatorContext) {
        return authorization == null || Arrays.asList(authorizationSet).contains(authorization);
    }
}
