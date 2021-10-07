package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Header {

    ASSOCIATION_ORIGIN("association-origin"),
    DOCUMENT_NUMBER("document-number"),
    DOCUMENT_TYPE("document-type");

    private final String name;
}
