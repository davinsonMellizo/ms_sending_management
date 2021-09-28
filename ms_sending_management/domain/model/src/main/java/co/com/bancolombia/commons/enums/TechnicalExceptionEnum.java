package co.com.bancolombia.commons.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalExceptionEnum {

    FIND_ALERT_BY_ID_ERROR("DST0001", "An error occurred while find a alert"),
    SAVE_ALERT_ERROR("DST0002", "An error occurred while save a alert"),
    UPDATE_ALERT_ERROR("DST0003", "An error occurred while update a alert"),
    DELETE_ALERT_ERROR("DST0004", "An error occurred while delete a alert"),
    FIND_ALL_ALERT_TRANSACTION_ERROR("DST0001", "An error occurred while find the relations alert with transaction"),
    SAVE_ALERT_TRANSACTION_ERROR("DST0002", "An error occurred while save a relation alert with transaction"),
    DELETE_ALERT_TRANSACTION_ERROR("DST0004", "An error occurred while delete a relation alert with transaction"),
    FIND_REMITTER_BY_ID_ERROR("DST0004", "An error occurred while find a remitter"),
    FIND_ALL_REMITTERS_ERROR("DST0005", "An error occurred while find all remitters"),
    SAVE_REMITTER_ERROR("DST0006", "An error occurred while save a remitter"),
    UPDATE_REMITTER_ERROR("DST0007", "An error occurred while update a remitter"),
    DELETE_REMITTER_ERROR("DST0008", "An error occurred while delete a remitter"),
    FIND_PROVIDER_BY_ID_ERROR("DST0009", "An error occurred while find a provider"),
    FIND_ALL_PROVIDERS_ERROR("DST0010", "An error occurred while find all providers"),
    SAVE_PROVIDER_ERROR("DST0011", "An error occurred while save a provider"),
    UPDATE_PROVIDER_ERROR("DST0012", "An error occurred while update a provider"),
    DELETE_PROVIDER_ERROR("DST0013", "An error occurred while delete a provider"),
    FIND_SERVICE_BY_ID_ERROR("DST0014", "An error occurred while find a service"),
    SAVE_SERVICE_ERROR("DST0015", "An error occurred while save a service"),
    UPDATE_SERVICE_ERROR("DST0016", "An error occurred while update a service"),
    DELETE_SERVICE_ERROR("DST0017", "An error occurred while delete a service"),
    FIND_ALL_ALERT_CLIENT_ERROR("DST0018", "An error occurred while found all alert client registers"),
    FIND_ALERT_CLIENT_ERROR("DST0018", "An error occurred while found alert client register"),
    SAVE_ALERT_CLIENT_ERROR("DST0019", "An error occurred while saved a relation alert with client"),
    UPDATE_ALERT_CLIENT_ERROR("DST0020", "An error occurred while updated a relation alert with client"),
    DELETE_ALERT_CLIENT_ERROR("DST0021", "An error occurred while deleted a relation alert with client"),
    SAVE_ALERT_TEMPLATE_ERROR("DST0022", "An error occurred while saved a alert template"),
    FIND_ALERT_TEMPLATE_BY_ID_ERROR("DST0023", "An error occurred while saved a alert template"),
    DELETE_ALERT_TEMPLATE_ERROR("DST0024", "An error occurred while saved a alert template"),
    FIND_CLIENT_ERROR("DST0025", "An error occurred while looking a client"),
    FIND_ALL_CONTACT_BY_CLIENT_ERROR("DST0026", "An error occurred while looking all contacts by client"),
    FIND_ALL_CONSUMER_ERROR("DST0027", "An error occurred while found all consumers"),
    FIND_CONSUMER_BY_ID_ERROR("DST0028", "An error occurred while found a consumer"),
    SAVE_CONSUMER_ERROR("DST0029", "An error occurred while save a consumer"),
    UPDATE_CONSUMER_ERROR("DST0030", "An error occurred while update a consumer"),
    DELETE_CONSUMER_ERROR("DST0031", "An error occurred while delete a consumer"),
    SEND_LOG_SQS_ERROR("DST0032","An error occurred while sending a log "),
    TECHNICAL_JSON_CONVERT("DST0033","An error occurred while converting object to json "),
    SECRET_EXCEPTION("DST0034", "An error occurred while trying to get AWS secrets"),
    INTERNAL_SERVER_ERROR("DST0035", "Internal server error"),
    BODY_MISSING_ERROR("301", "Missing parameters per body"),
    HEADER_MISSING_ERROR("301", "Missing parameters per header");

    private final String code;
    private final String message;


}
