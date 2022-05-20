package co.com.bancolombia.events.model;

import lombok.Data;

import java.util.List;

@Data
public class ResourceQuery {

    private List<Resource> data;

    @Data
    public static class Resource {
        private String queryName;
        private String channel;
        private String transaction;
        private String template;
    }
}
