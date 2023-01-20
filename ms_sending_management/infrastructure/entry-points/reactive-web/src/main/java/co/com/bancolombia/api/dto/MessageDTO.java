package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.message.Attachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MessageDTO {
    @Builder.Default
    private ArrayList<String> preferences= new ArrayList<>();
    @Builder.Default
    private String url = "";
    @Builder.Default
    private Map<String, String> parameters = new HashMap<>();
    @Builder.Default
    private ArrayList<Attachment> attachments = new ArrayList<>();
    @Builder.Default
    private String remitter = "";
    private Integer priority;
    @Builder.Default
    private String template = "";

}
