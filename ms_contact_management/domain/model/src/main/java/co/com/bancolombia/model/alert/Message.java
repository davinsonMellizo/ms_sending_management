package co.com.bancolombia.model.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Message {
    private String category;
    private ArrayList<String> preferences;
    private Map<String, String> parameters;
    private Sms sms;
    private Mail mail;
    private Push push;

}
