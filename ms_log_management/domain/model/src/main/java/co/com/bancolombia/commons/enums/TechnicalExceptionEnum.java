package co.com.bancolombia.commons.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalExceptionEnum {

    SAVE_LOG_ERROR("DST0001", "An error has occurred saving the log"),
    FIND_LOG_ERROR("DST0002", "An error has occurred searching the log"),
    INTERNAL_SERVER_ERROR("DST0003", "Internal server error"),
    BODY_MISSING_ERROR("301", "Missing parameters per body"),
    HEADER_ERROR("302", "Error with sent filters");

    private final String code;
    private final String message;


}
