package co.com.bancolombia.consumer.adapter.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SuccessInfobipSMS {
    private String deliveryToken;
}


/*
{
    "messages": [
        {
            "messageId": "37064058886643357094",
            "status": {
                "description": "Message sent to next instance",
                "groupId": 1,
                "groupName": "PENDING",
                "id": 26,
                "name": "PENDING_ACCEPTED"
            },
            "to": "573172550373"
        }
    ]
}
*/
