package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Header {

    public static final String ASSOCIATION_ORIGIN = "association-origin";
    public static final String DOCUMENT_NUMBER = "document-number";
    public static final String CONSUMER_CODE = "consumer";
    public static final String DOCUMENT_TYPE = "document-type";
    public static final String CONTACT_MEDIUM = "contact-medium";
    public static final String SEGMENT = "segment";
    public static final String CONSUMER = "consumer";
    public static final String DOCUMENT_NUMBER_INIT = "document-number-init";
    public static final String DOCUMENT_NUMBER_END = "document-number-end";

    private final String name;
}
