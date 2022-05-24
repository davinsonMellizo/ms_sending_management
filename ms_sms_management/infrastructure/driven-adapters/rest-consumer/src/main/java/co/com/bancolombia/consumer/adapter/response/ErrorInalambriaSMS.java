package co.com.bancolombia.consumer.adapter.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInalambriaSMS{
    private String MessageText;
    private Integer Status;
    private Long TransactionNumber;
}
