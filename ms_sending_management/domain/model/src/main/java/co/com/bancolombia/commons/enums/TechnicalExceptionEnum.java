package co.com.bancolombia.commons.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalExceptionEnum {

    FIND_ALERT_BY_ID_ERROR("DST0001", "An error occurred while find a alert"),
    FIND_ALL_ALERT_TRANSACTION_ERROR("DST0001", "An error occurred while find the relations alert with transaction"),
    FIND_REMITTER_BY_ID_ERROR("DST0004", "An error occurred while find a remitter"),
    FIND_PROVIDER_BY_ID_ERROR("DST0009", "An error occurred while find a provider"),
    FIND_SERVICE_BY_ID_ERROR("DST0014", "An error occurred while find a service"),
    FIND_ALERT_CLIENT_ERROR("DST0018", "An error occurred while found alert client register"),
    ACCUMULATE_ALERT_CLIENT_ERROR("DST0020", "An error occurred while accumulate a alert with client"),
    FIND_CLIENT_ERROR("DST0025", "An error occurred while looking a client"),
    FIND_ALL_CONTACT_BY_CLIENT_ERROR("DST0026", "An error occurred while looking all contacts by client"),
    FIND_CONSUMER_BY_ID_ERROR("DST0028", "An error occurred while found a consumer"),
    FIND_CATEGORY_BY_ID_ERROR("DST0032", "An error occurred while find a category"),
    FIND_PROVIDER_SERVICE_ERROR("DST0037", "An error occurred while find the provider service"),
    SEND_LOG_SQS_ERROR("DST0040", "An error occurred while sending a log "),
    TECHNICAL_JSON_CONVERT("DST0041", "An error occurred while converting object to json "),
    SECRET_EXCEPTION("302", "An error occurred while trying to get AWS secrets"),
    INTERNAL_SERVER_ERROR("302", "Internal server error"),
    BODY_MISSING_ERROR("301", "Missing parameters per body"),
    INVALID_HEADER_ERROR("301", "Invalid headers"),
    HEADER_MISSING_ERROR("301", "Missing parameters per header");

    private final String code;
    private final String message;


}
