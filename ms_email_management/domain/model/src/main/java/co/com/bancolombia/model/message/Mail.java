package co.com.bancolombia.model.message;

import co.com.bancolombia.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Mail extends Request {
    private String Subject;
    private String From;
    private TemplateMasivian Template;
    private String PreviewText;
    private String ReplyTo;
    private List<Parameter> parameters;
    private List<Recipient> Recipients;
    private List<Attachment> Attachments;

    private String nameToken;


}
