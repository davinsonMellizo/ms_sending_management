package co.com.bancolombia.commons.constants;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class Provider {
    public static final String SES = "SES";
    public static final String TOD = "TOD";
    public static final String MASIVIAN = "MAS";
}
