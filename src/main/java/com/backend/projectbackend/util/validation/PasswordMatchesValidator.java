package com.backend.projectbackend.util.validation;

import com.backend.projectbackend.dto.auth.AuthCreateAccountDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, AuthCreateAccountDTO> {

    @Override
    public boolean isValid(AuthCreateAccountDTO dto, ConstraintValidatorContext context) {
        if (dto.getPassword() == null || dto.getPasswordConfirm() == null) {
            return false;
        }
        return dto.getPassword().equals(dto.getPasswordConfirm());
    }
}
