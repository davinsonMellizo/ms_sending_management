package co.com.bancolombia.usecase.sendalert.commons;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Medium {
    public static String PUSH = "PUSH";
    public static String SMS = "SMS";
}
