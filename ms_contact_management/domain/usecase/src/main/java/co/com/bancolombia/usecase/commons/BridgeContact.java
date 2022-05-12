package co.com.bancolombia.usecase.commons;

import co.com.bancolombia.commons.enums.DocumentTypeEnum;
import co.com.bancolombia.model.bridge.Bridge;
import co.com.bancolombia.model.client.Enrol;
import co.com.bancolombia.model.contact.Contact;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static co.com.bancolombia.commons.constants.ContactWay.*;

@UtilityClass
public class BridgeContact {

    public final int ZERO = 0;
    public final int ONE = 1;

    public Bridge getMapToSendToBridgeMQ(Enrol enrol, String voucher) {
        return Bridge.builder()
                .documentType(String.valueOf(DocumentTypeEnum
                        .fromValue(enrol.getClient().getDocumentType()).getId()))
                .documentNumber(String.valueOf(enrol.getClient().getDocumentNumber()))
                .channel(enrol.getClient().getConsumerCode())
                .valueSms(getValue(enrol, SMS))
                .valueEmail(getValue(enrol, MAIL))
                .valuePush(getValue(enrol, PUSH))
                .stateSms(getState(enrol, SMS))
                .stateMEmail(getState(enrol, MAIL))
                .statePush(getState(enrol, PUSH))
                .voucher(voucher)
                .date(LocalDateTime.now().toString())
                .creationUser(enrol.getClient().getCreationUser())
                .build();
    }

    private String getValue(Enrol enrol, String contactWay) {
        return enrol.getContactData().stream().filter(contact -> contact.getContactWay()
                .equals(contactWay)).map(Contact::getValue).collect(Collectors.joining());
    }

    private String getState(Enrol enrol, String contactWay) {
        final var state = enrol.getContactData().stream().filter(contact -> contact.getContactWay()
                .equals(contactWay)).map(Contact::getStateContact).collect(Collectors.joining());
        return state.isEmpty() ? state : state.substring(ZERO, ONE);
    }

    public String getVoucher() {
        final var random = ThreadLocalRandom.current();
        return String.valueOf(random.nextLong(1000000000L,
                10000000000L));
    }
}
