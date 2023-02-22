package co.com.bancolombia.api.commons.validators.constraints;

import co.com.bancolombia.api.commons.validators.ProviderFormatValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ProviderFormatValidator.class)
@Target({TYPE, FIELD})
@Retention(RUNTIME)
public @interface ProviderFormat {

    String message() default "el formato del proveedor debe contener idProvider y channelType";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    @Target({TYPE, FIELD})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ProviderFormat[] value();
    }
}
