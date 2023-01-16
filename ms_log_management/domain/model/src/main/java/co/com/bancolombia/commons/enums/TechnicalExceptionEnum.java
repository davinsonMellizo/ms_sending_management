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
    ERROR_GLUE_ETL_AWS_SDK("DST0004", "Error trying to execute Glue ETL Job"),
    ERROR_GLUE_ETL_MISSING_MANDATORY_ARGUMENTS("DST0005", "Mandatory arguments for Glue ETL Job are missing"),
    HEADER_ERROR("302", "Error with sent filters");

    private final String code;
    private final String message;


}
