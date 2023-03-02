package co.com.bancolombia.commons.constants;

public class Constants {

    public static final int ONE = 1;
    public static final int PRIME = 31;

    public static final String ENABLED = "1";
    public static final String DISABLED = "0";
    public static final String KEY_OPEN = "${";
    public static final String KEY_CLOSE = "}";


    private Constants() {
        throw new IllegalStateException("Utility class");
    }
}
