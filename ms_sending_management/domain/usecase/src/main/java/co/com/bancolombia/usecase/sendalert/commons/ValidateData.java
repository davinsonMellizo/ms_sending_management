package co.com.bancolombia.usecase.sendalert.commons;

import co.com.bancolombia.model.message.Message;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;


public abstract class ValidateData {
    private static final String PATTERN =  "^(([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+)+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$";

    public static final Predicate<Message> isValidMailOrMobile = message ->
            (Objects.nonNull(message.getMail()) && !message.getMail().isEmpty())
                    || (Objects.nonNull(message.getPhone()) && !message.getPhone().isEmpty());

    public static final Predicate<Message> isValidMobile = message ->
            Objects.nonNull(message.getPhone()) && !message.getPhone().isEmpty();

    public static final Predicate<Message> isValidMailFormat = message ->
            (Objects.nonNull(message.getMail()) && !message.getMail().isEmpty())
            && Pattern.compile(PATTERN).matcher(message.getMail()).matches();

    public static final Predicate<Message> isValidMail = message ->
            Objects.nonNull(message.getMail()) && !message.getMail().isEmpty();

    public static final Predicate<Message> isValidMailFormatOrMobile = message ->
            (Objects.nonNull(message.getPhone()) && !message.getPhone().isEmpty())
                    || ((Objects.nonNull(message.getMail()) && !message.getMail().isEmpty())
                    && Pattern.compile(PATTERN).matcher(message.getMail()).matches());


}
