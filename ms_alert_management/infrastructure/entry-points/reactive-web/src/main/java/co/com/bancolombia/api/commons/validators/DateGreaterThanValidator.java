package co.com.bancolombia.api.commons.validators;

import co.com.bancolombia.api.commons.validators.constraints.DateGreaterThan;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateGreaterThanValidator implements ConstraintValidator<DateGreaterThan, Object> {
    private String startDate;
    private String endDate;

    @Override
    public void initialize(DateGreaterThan constraintAnnotation) {
        this.startDate = constraintAnnotation.startDate();
        this.endDate = constraintAnnotation.endDate();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        LocalDate startDateValue = (LocalDate) new BeanWrapperImpl(value)
                .getPropertyValue(startDate);
        LocalDate endDateValue = (LocalDate) new BeanWrapperImpl(value)
                .getPropertyValue(endDate);

        if (startDateValue == null || endDateValue == null) {
            return true;
        }

        return endDateValue.isAfter(startDateValue);
    }
}
