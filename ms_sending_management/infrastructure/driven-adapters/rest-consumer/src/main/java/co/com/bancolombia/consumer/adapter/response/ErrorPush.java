package co.com.bancolombia.consumer.adapter.response;

import java.util.List;

public class ErrorPush extends Error{
    private String status;
    private String title;
    private List<Error> errors;


    public static class Error{
        private String code;
        private String detail;
    }
}
