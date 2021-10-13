package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Parameter {

    DOCUMENT_NUMBER("document-number","document_number"),
    DOCUMENT_TYPE("document-type", "document_type"),
    CONSUMER("consumer", "consumer"),
    CONTACT("contact","contact"),
    START_DATE("start-date", "date_creation"),
    END_DATE("end-date", "date_creation");

    private final String nameHeader;
    private final String nameColumn;


}
