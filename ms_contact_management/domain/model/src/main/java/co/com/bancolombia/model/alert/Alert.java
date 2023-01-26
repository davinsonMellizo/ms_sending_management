package co.com.bancolombia.model.alert;

import co.com.bancolombia.model.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Alert extends Request {
    private Boolean retrieveInformation;
    private ClientMessage client;
    private Message message;
    private AlertParameters alertParameters;

}
