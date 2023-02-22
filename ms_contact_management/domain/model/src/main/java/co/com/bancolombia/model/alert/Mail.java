package co.com.bancolombia.model.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Mail implements Serializable {

    private String address;
    private String remitter;
    private String template;
    private ArrayList<Attachment> attachments;
}
