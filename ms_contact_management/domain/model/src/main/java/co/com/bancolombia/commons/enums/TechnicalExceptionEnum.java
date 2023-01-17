package co.com.bancolombia.commons.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalExceptionEnum {

    FIND_ALL_CONTACT_BY_CLIENT_ERROR("T0001", "An error occurred while find contacts", "500"),
    SAVE_CONTACT_ERROR("T0002", "An error occurred while save contact", "500"),
    UPDATE_CONTACT_ERROR("T0003", "An error occurred while update contact","500"),
    DELETE_CONTACT_ERROR("T0004", "An error occurred while delete contact","500"),
    FIND_CONTACT_ERROR("T0005", "An error occurred while find contact","500"),
    SAVE_CLIENT_ERROR("T0006", "An error occurred while save client","500"),
    UPDATE_CLIENT_ERROR("T0007", "An error occurred while update client","500"),
    FIND_CLIENT_ERROR("T0009", "An error occurred while find client","500"),
    FIND_STATE_ERROR("T0010", "An error occurred while find state","500"),
    FIND_CONTACT_MEDIUM_ERROR("T0011", "An error occurred while find contact medium","500"),
    FIND_DOCUMENT_ERROR("T0013", "An error occurred while find a document","500"),
    FIND_CONSUMER_BY_ID_ERROR("T0014", "An error occurred while find a consumer","500"),
    INACTIVE_CLIENT_ERROR("T0015", "An error occurred while active/inactive the client","500"),
    INTERNAL_SERVER_ERROR("T0018", "Internal server error","500"),
    UNAUTHORIZED("T0019", "Invalid client id or secret for iseries","500"),
    HEADERS_MISSING_ERROR("T0301", "Missing parameters per Headers","400"),
    BODY_MISSING_ERROR("T0302", "Missing parameters per body","400");

    private final String code;
    private final String message;
    private final String httpStatus;

}
