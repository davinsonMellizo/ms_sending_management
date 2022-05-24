package co.com.bancolombia.sqs.commons;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.TECHNICAL_JSON_CONVERT;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class Util {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertToJson(Object model) {
        try {
            return objectMapper.writeValueAsString(model);
        }catch (JsonProcessingException ex){
            throw new TechnicalException(TECHNICAL_JSON_CONVERT);
        }
    }
}
