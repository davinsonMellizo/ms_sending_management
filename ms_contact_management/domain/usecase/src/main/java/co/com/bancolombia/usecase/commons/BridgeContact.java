package co.com.bancolombia.usecase.commons;

import co.com.bancolombia.model.bridge.Bridge;
import co.com.bancolombia.model.client.Enrol;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.document.Document;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static co.com.bancolombia.commons.constants.ContactWay.*;

@UtilityClass
public class BridgeContact {

    public final int ZERO = 0;
    public final int ONE = 1;

    public Bridge getMapToSendToBridgeMQ(Enrol enrol, String voucher, Document document) {
        return Bridge.builder()
                .documentType(document.getId())
                .documentNumber(String.valueOf(enrol.getClient().getDocumentNumber()))
                .channel(enrol.getClient().getConsumerCode())
                .valueSms(getValue(enrol, SMS))
                .valueEmail(getValue(enrol, MAIL))
                .valuePush(getValue(enrol, PUSH))
                .stateSms(getState(enrol, SMS))
                .stateEmail(getState(enrol, MAIL))
                .statePush(getState(enrol, PUSH))
                .voucher(voucher)
                .date(LocalDateTime.now().toString())
                .creationUser(enrol.getClient().getCreationUser())
                .build();
    }

    private String getValue(Enrol enrol, String contactWay) {
        return enrol.getContactData().stream()
                .filter(contact -> getName(contact).equals(contactWay))
                .map(Contact::getValue)
                .collect(Collectors.joining());
    }
    private String getName(Contact contact){
        return contact.getContactWayName() != null ? contact.getContactWayName() : contact.getContactWay();
    }

    private String getState(Enrol enrol, String contactWay) {
        final var state = enrol.getContactData().stream()
                .filter(contact -> getName(contact).equals(contactWay))
                .map(Contact::getStateContact)
                .collect(Collectors.joining());
        return state.isEmpty() ? state : state.substring(ZERO, ONE);
    }

    public String getVoucher() {
        final var random = ThreadLocalRandom.current();
        return String.valueOf(random.nextLong(1000000000L,
                10000000000L));
    }
}
