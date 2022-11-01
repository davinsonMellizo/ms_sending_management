package co.com.bancolombia.api.commons.validators.constraints;

import co.com.bancolombia.api.commons.validators.FieldsValueMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = FieldsValueMatchValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface FieldsValueMatch {
    String message() default "Fields values don't match";

    String field();

    String fieldMatch();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({TYPE})
    @Retention(RUNTIME)
    @interface List {
        FieldsValueMatch[] value();
    }
}
