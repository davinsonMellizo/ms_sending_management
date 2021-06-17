package co.com.bancolombia.commons.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalExceptionEnum {

    FIND_STATE_ERROR("DST0000", "An error occurred while find a state"),
    FIND_ALERT_BY_ID_ERROR("DST0001", "An error occurred while find a alert"),
    SAVE_ALERT_ERROR("DST0002", "An error occurred while save a alert"),
    UPDATE_ALERT_ERROR("DST0003", "An error occurred while update a alert"),
    DELETE_ALERT_ERROR("DST0004", "An error occurred while delete a alert"),
    SECRET_EXCEPTION("DST0009", "An error occurred while trying to get AWS secrets"),
    INTERNAL_SERVER_ERROR("DST0010", "Internal server error"),
    BODY_MISSING_ERROR("301", "Missing parameters per body");

    private final String code;
    private final String message;


}
