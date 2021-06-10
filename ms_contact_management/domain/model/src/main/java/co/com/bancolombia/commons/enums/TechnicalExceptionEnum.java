package co.com.bancolombia.commons.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalExceptionEnum {

    FIND_ALL_CONTACT_BY_CLIENT_ERROR("DST0001", "An error occurred while save contact"),
    SAVE_CONTACT_ERROR("DST0002", "An error occurred while save contact"),
    UPDATE_CONTACT_ERROR("DST0003", "An error occurred while update contact"),
    DELETE_CONTACT_ERROR("DST0004", "An error occurred while delete contact"),
    FIND_CONTACT_ERROR("DST0005", "An error occurred while find contact"),
    FIND_STATE_ERROR("DST0006", "An error occurred while find state"),
    FIND_CONTACT_MEDIUM_ERROR("DST0007", "An error occurred while find contact medium"),
    FIND_ENROLLMENT_CONTACT_ERROR("DST0008", "An error occurred while find enrollment contact"),
    SECRET_EXCEPTION("DST0009", "An error occurred while trying to get AWS secrets"),
    INTERNAL_SERVER_ERROR("DST0010", "Internal server error"),
    HEADERS_MISSING_ERROR("301", "Missing parameters per Headers"),
    BODY_MISSING_ERROR("301", "Missing parameters per body");

    private final String code;
    private final String message;


}