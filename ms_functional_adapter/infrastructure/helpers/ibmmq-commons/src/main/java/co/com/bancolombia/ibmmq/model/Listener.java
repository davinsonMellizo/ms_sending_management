package co.com.bancolombia.ibmmq.model;

import lombok.Data;

@Data
public class Listener {
    private String name;
    private String queueRequest;
    private String queueResponse;
}
