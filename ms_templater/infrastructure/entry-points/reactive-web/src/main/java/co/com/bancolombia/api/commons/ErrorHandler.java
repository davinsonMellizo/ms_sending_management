package co.com.bancolombia.api.commons;
import co.com.bancolombia.api.utils.ErrorDTO;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;

public class ErrorHandler {
    public static ErrorDTO business(BusinessException object) {
        return ErrorDTO.builder()
                .code(object.getException().getCode())
                .type("Business")
                .title(object.getException().toString())
                .detail(object.getException().getMessage())
                .source("ms_templater")
                .build();
    }

    public static ErrorDTO technical(TechnicalException object) {
        return ErrorDTO.builder()
                .code(object.getException().getCode())
                .type("Technical")
                .title(object.getException().toString())
                .detail(object.getException().getMessage())
                .source("ms_templater")
                .build();
    }
}
