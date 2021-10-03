package io.wisoft.vamos.config.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FileLengthValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileArrayLength {

    String message() default "Invalid File length.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
