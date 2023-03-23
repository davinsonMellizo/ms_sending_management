package karate.process_contacts;

import com.intuit.karate.junit5.Karate;

public class ProcessContactsTest {

    @Karate.Test
    Karate checkProcessContacts() {
        return Karate.run("process_contacts.feature").relativeTo(getClass());
    }
}
