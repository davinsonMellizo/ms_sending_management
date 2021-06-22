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
    SECRET_EXCEPTION("DST0018", "An error occurred while trying to get AWS secrets"),
    INTERNAL_SERVER_ERROR("DST0019", "Internal server error"),
    BODY_MISSING_ERROR("301", "Missing parameters per body");

    private final String code;
    private final String message;


}
