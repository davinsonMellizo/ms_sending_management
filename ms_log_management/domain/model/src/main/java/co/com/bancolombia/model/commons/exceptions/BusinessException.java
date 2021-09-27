package co.com.bancolombia.model.commons.exceptions;

import co.com.bancolombia.model.commons.enums.BusinessErrorMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BusinessException extends Exception {
    private final BusinessErrorMessage businessErrorMessage;
}