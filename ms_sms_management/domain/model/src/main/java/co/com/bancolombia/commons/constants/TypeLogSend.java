package co.com.bancolombia.commons.constants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor
@Getter
@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class TypeLogSend {

    public static final String SEND_220 = "220";
    public static final String SEND_230 = "230";

}
