package co.com.bancolombia.consumer.adapter.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMasivianMAIL{
    private String status;
    private String description;
    private String data;
}
