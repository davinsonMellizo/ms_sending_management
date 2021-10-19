package co.com.bancolombia.consumer;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Commons {
    private static final String HOST = "http://localhost:%s/";
    public static String getBaseUrl(int port){
        return String.format(HOST, port);
    }
}
