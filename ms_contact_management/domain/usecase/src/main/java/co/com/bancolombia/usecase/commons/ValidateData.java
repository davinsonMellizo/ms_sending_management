package co.com.bancolombia.usecase.commons;

import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.message.Message;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;


public abstract class ValidateData {
    private static final String PATTERN =  "^(([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+)+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$";
    private static final String PATTERN_MOBILE =  "[0-9]+";

    public static final Predicate<Contact> isValidMailOrMobile = contact ->
            (Objects.nonNull(contact.getValue()) && !contact.getValue().isEmpty() &&
                    Pattern.compile(PATTERN_MOBILE).matcher(contact.getValue()).matches())
                    || (Objects.nonNull(contact.getValue()) && !contact.getValue().isEmpty()
                    && Pattern.compile(PATTERN).matcher(contact.getValue()).matches());

}
