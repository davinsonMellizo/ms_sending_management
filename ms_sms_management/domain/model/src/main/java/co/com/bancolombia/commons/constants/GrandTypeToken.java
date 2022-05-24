package co.com.bancolombia.commons.constants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@RequiredArgsConstructor
@Getter
@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class GrandTypeToken {


    public static final String GRAND_TYPE = "password";
    public static final String GRAND_TYPE_REFRESH = "refresh_token";

}

