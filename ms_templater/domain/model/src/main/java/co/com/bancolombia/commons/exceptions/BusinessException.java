package co.com.bancolombia.commons.exceptions;

import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BusinessException extends RuntimeException {
    private final BusinessExceptionEnum exception;
}
