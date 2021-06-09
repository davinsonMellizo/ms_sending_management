package co.com.bancolombia.model.response;

import co.com.bancolombia.model.alert.Alert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ContactsResponse {
    private List<Alert> alerts;
}
