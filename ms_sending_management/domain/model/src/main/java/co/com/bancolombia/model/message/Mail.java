package co.com.bancolombia.model.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Mail {
    private String Subject;
    private String From;
    private Template Template;
    private String PreviewText;
    private String ReplyTo;
    private ArrayList<Parameter> parameters;
    private ArrayList<Recipient> Recipients;
    private ArrayList<Attachment> Attachments;


}
