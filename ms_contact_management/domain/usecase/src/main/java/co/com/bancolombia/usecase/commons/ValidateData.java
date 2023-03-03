package co.com.bancolombia.usecase.commons;

import co.com.bancolombia.model.contact.Contact;
import lombok.experimental.UtilityClass;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@UtilityClass
public class ValidateData {

    private static final String PATTERN = "^\\(\\+\\d{1,6}+\\)\\d{8,}+$";
    public static final Predicate<Contact> validatePhoneNumber = contact ->
            !Pattern.compile(PATTERN).matcher(contact.getValue()).find();

}
