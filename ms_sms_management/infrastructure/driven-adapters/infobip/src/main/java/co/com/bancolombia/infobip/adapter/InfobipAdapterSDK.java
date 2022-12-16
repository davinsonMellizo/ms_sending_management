package co.com.bancolombia.infobip.adapter;

import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.SMSInfobip;
import co.com.bancolombia.model.message.SMSInfobipSDK;
import co.com.bancolombia.model.message.gateways.InfobipGateway;
import co.com.bancolombia.model.token.Account;
import co.com.bancolombia.model.token.Token;
import co.com.bancolombia.model.token.TokenInfobip;
import com.infobip.ApiClient;
import com.infobip.ApiException;
import com.infobip.api.SendSmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsResponse;
import com.infobip.model.SmsTextualMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Collections;


@Repository
@RequiredArgsConstructor
public class InfobipAdapterSDK implements InfobipGateway {

    private static final Integer STATUS_OK = 200;

    @Override
    public Mono<Response> sendSMSSDK(SMSInfobipSDK smsInfobip) {
        return Mono.just(initApiClient())
                .map(sendSmsApi-> {
                    try {
                        SmsResponse smsResponse = sendSmsApi.sendSmsMessage(createSmsMessage(smsInfobip));
                        return Response.builder().code(STATUS_OK)
                                .description(smsResponse.toString()).build();
                    } catch (ApiException e) {
                        return Response.builder()
                                .code(e.getCode()).description(e.getMessage())
                                .build();
                    }
                });
    }

    public SendSmsApi initApiClient() {
        ApiClient client = new ApiClient("URL_BASE_PATH", "CLIENT_ID", "CLIENT_SECRET", Collections.emptyMap());
        return new SendSmsApi(client);
    }

    public SmsAdvancedTextualRequest createSmsMessage(SMSInfobipSDK smsInfobip) {
        SmsTextualMessage smsMessage = new SmsTextualMessage()
                .from("SENDER")
                .addDestinationsItem(new SmsDestination().to("RECIPIENT"))
                .text("MESSAGE_TEXT");
        return new SmsAdvancedTextualRequest().messages(
                Collections.singletonList(smsMessage)
        );
    }

    @Override
    public Mono<Token> getToken(Account account) {
        return null;
    }

    @Override
    public Mono<Response> sendSMS(SMSInfobip smsInfobip) {
        return null;
    }

}
