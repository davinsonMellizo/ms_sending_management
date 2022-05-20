package co.com.bancolombia.commons.utils;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BusinessUtil {


    public static Object getValue(Map<String, Object> data, String value) {
        return data.get(value);
    }


}
