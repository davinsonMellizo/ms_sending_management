package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorMessage {

    REQUIRED_MESSAGE_TEMPLATE("BS0001", "Required message or template", false),
    SECRET_NAME_NOT_FOUND("BS0002", "Provider Account not found for priority and id provider", false),
    TRANSACTION_OK("B0004", "Successful transaction",false),
    TEMPLATE_NOT_FOUND("BS0003", "Template not found", false);



    private final String code;
    private final String message;
    private final Boolean retry;
}