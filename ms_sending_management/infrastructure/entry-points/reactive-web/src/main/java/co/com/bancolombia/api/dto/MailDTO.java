package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.message.Attachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MailDTO {

    @Builder.Default
    private String address = "";
    @Builder.Default
    private String remitter = "";
    @Builder.Default
    private ArrayList<Attachment> attachments = new ArrayList<>();
}
