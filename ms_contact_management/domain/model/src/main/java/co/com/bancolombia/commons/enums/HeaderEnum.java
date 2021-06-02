package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HeaderEnum {

    DOCUMENT_TYPE("document-type"),
    DOCUMENT_NUMBER("document-number");

    private final String name;
}