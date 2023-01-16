package co.com.bancolombia.api.commons.validators.constraints;

import co.com.bancolombia.api.commons.validators.DateGreaterThanValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = DateGreaterThanValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface DateGreaterThan {
    String message() default "Start date must be greater than the end date";

    String startDate();

    String endDate();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({TYPE})
    @Retention(RUNTIME)
    @interface List {
        DateGreaterThan[] value();
    }

}
