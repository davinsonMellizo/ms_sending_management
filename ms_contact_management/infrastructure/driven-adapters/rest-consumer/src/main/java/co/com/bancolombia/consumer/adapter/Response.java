package co.com.bancolombia.consumer.adapter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Response {

    private DataResponse data;


}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
class DataResponse{
    private List<AlertIndicator> alertIndicators = new ArrayList<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
class AlertIndicator{
    private Long documentNumber;
    private String documentType;
    private String alertType;
    private String customerMobileNumber;
    private String customerEmail;
    private String pushActive;
    private LocalDate lastDataModificationDate;
}
