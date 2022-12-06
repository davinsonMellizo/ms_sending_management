package co.com.bancolombia.commons.constants;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@RequiredArgsConstructor
@Getter
@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class State {
    public static final Integer ACTIVE = 0;
    public static final Integer INACTIVE = 1;
}
