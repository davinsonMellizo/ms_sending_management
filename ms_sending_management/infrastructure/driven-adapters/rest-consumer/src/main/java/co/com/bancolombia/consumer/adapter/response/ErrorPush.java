package co.com.bancolombia.consumer.adapter.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorPush {
    private String status;
    private String title;
    private List<Error> errors;


    public static class Error {
        private String code;
        private String detail;
    }
}
