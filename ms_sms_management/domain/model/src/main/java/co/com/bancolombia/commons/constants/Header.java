package co.com.bancolombia.commons.constants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class Header {

    public static final String ASSOCIATION_ORIGIN = "association-origin";
    public static final String DOCUMENT_NUMBER = "document-number";
    public static final String DOCUMENT_TYPE = "document-type";

}
