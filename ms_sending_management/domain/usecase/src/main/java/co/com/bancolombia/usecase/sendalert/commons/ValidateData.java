package co.com.bancolombia.usecase.sendalert.commons;

import co.com.bancolombia.model.message.Message;

import java.util.function.Predicate;
import java.util.regex.Pattern;


public abstract class ValidateData {

    private static final String PATTERN = "^(([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+)+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$";

    public static final Predicate<Message> isValidMailOrMobile = message ->
            isNotEmpty(message.getMail()) || isNotEmpty(message.getPhone());

    public static final Predicate<Message> isValidMobile = message -> isNotEmpty(message.getPhone());

    public static final Predicate<Message> isValidMailFormat = message ->
            isNotEmpty(message.getMail()) && Pattern.compile(PATTERN).matcher(message.getMail()).matches();

    public static final Predicate<Message> isValidMail = message -> isNotEmpty(message.getMail());

    public static final Predicate<Message> isValidMailFormatOrMobile = message ->
            isNotEmpty(message.getPhone())
                    || (isNotEmpty(message.getMail()) && Pattern.compile(PATTERN).matcher(message.getMail()).matches());

    public static boolean isNotEmpty(String str) {
        return !str.isEmpty();
    }

}
