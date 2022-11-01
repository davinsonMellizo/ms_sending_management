package co.com.bancolombia.api.commons.validators.constraints;

import co.com.bancolombia.api.commons.validators.JSONFieldNotNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = JSONFieldNotNullValidator.class)
@Target({TYPE, FIELD})
@Retention(RUNTIME)
public @interface JSONFieldNotNull {

    String message() default "JSON field must not be null or empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({TYPE, FIELD})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        JSONFieldNotNull[] value();
    }

}
