package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessExceptionEnum {
    BUSINESS_CAMPAIGN_NOT_FOUND("BE001", "Campaign not found");

    private final String code;
    private final String message;
}
