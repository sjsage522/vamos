package io.wisoft.vamos.config.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class FileLengthValidator implements ConstraintValidator<FileArrayLength, MultipartFile[]> {

    @Override
    public boolean isValid(MultipartFile[] value, ConstraintValidatorContext context) {
        log.info("FileLengthValidator.isValid");
        return value == null || value.length <= 5;
    }
}
