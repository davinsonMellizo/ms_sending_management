package co.com.bancolombia.usecase.commons;

import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.newness.Newness;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactoryLog {
    private Newness createLog(Client client){
        return null;
    }

    private Newness createLog(Contact contact){
        return null;
    }
}
