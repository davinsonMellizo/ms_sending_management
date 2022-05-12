package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TemplateType {

    COMPLEX("masiv-template/html", ""),
    SIMPLE("text/html", "<div>?</div>");

    private final String type;
    private final String value;
}
