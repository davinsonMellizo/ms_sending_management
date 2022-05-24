package co.com.bancolombia.usecase.sendalert.commons;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@RequiredArgsConstructor
@Getter
@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class Medium {
    public static final String PUSH = "PUSH";
    public static final String SMS = "SMS";
}
