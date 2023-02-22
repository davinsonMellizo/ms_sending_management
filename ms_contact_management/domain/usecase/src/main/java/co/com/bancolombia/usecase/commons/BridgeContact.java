package co.com.bancolombia.usecase.commons;

import co.com.bancolombia.model.bridge.Bridge;
import co.com.bancolombia.model.client.Enrol;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.document.Document;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static co.com.bancolombia.commons.constants.ContactWay.MAIL;
import static co.com.bancolombia.commons.constants.ContactWay.PUSH;
import static co.com.bancolombia.commons.constants.ContactWay.SMS;

@UtilityClass
public class BridgeContact {

    public final int ZERO = 0;
    public final int ONE = 1;

    public static Bridge getMapToSendToBridgeMQ(Enrol enrol, String voucher, Document document) {
        return Bridge.builder()
                .documentType(document.getId())
                .documentNumber(String.valueOf(enrol.getClient().getDocumentNumber()))
                .channel(enrol.getClient().getConsumerCode())
                .valueSms(getValue(enrol.getContactData(), SMS))
                .valueEmail(getValue(enrol.getContactData(), MAIL))
                .valuePush(getValue(enrol.getContactData(), PUSH))
                .stateSms(getState(enrol, SMS))
                .stateEmail(getState(enrol, MAIL))
                .statePush(getState(enrol, PUSH))
                .voucher(voucher)
                .date(LocalDateTime.now().toString())
                .creationUser(enrol.getClient().getCreationUser())
                .build();
    }

    public static String getValue(List<Contact> contacts, String contactWay) {
        return contacts.stream()
                .filter(contact -> getName(contact).equals(contactWay))
                .map(Contact::getValue)
                .collect(Collectors.joining());
    }
    public static String getName(Contact contact){
        return contact.getContactWayName() != null ? contact.getContactWayName() : contact.getContactWay();
    }

    private static String getState(Enrol enrol, String contactWay) {
        final var state = enrol.getContactData().stream()
                .filter(contact -> getName(contact).equals(contactWay))
                .map(Contact::getStateContact)
                .collect(Collectors.joining());
        return state.isEmpty() ? state : state.substring(ZERO, ONE);
    }

    public static String getVoucher() {
        final var random = ThreadLocalRandom.current();
        return String.valueOf(random.nextLong(1000000000L,
                10000000000L));
    }
}
