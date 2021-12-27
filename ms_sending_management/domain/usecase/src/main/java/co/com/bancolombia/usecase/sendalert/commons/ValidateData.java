package co.com.bancolombia.usecase.sendalert.commons;

import co.com.bancolombia.model.message.Message;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;


public abstract class ValidateData {

    private static final String PATTERN = "^(([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+)+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$";

    public static final Predicate<Message> isValidMailOrMobile = message ->
            isNotNull(message.getMail()) || isNotNull(message.getPhone());

    public static final Predicate<Message> isValidMobile = message -> isNotNull(message.getPhone());

    public static final Predicate<Message> isValidMailFormat = message ->
            isNotNull(message.getMail()) && Pattern.compile(PATTERN).matcher(message.getMail()).matches();

    public static final Predicate<Message> isValidMail = message -> isNotNull(message.getMail());

    public static final Predicate<Message> isValidMailFormatOrMobile = message ->
            isNotNull(message.getPhone())
                    || (isNotNull(message.getMail()) && Pattern.compile(PATTERN).matcher(message.getMail()).matches());

    public static boolean isNotNull(String str) {
        return Objects.nonNull(str) || !str.isEmpty();
    }

}
