package co.com.bancolombia.api.commons.validators.constraints;

import co.com.bancolombia.api.commons.validators.CheckBooleanValueForFieldRequiredValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = CheckBooleanValueForFieldRequiredValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface CheckBooleanValueForFieldRequired {
    String message() default "Required field must not be null";

    String checkedField();

    boolean checkedValue() default false;

    String requiredField();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({TYPE})
    @Retention(RUNTIME)
    @interface List {
        CheckBooleanValueForFieldRequired[] value();
    }
}
