package co.com.bancolombia.commons.constants;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor
@Getter
@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class Provider {
    public static final String INALAMBRIA = "INA";
    public static final String MASIVIAN = "MAS";
    public static final String INFOBIP = "INF";
    public static final String CONTACTABILIDAD = "PUSH";
}
