package co.com.bancolombia.usecase.sendalert.commons;

import co.com.bancolombia.model.message.Message;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public abstract class ValidateData {

    public static final Predicate<Message> isValidMailAndMobile = message ->
            (Objects.nonNull(message.getMail()) && !message.getMail().isEmpty())
                    || (Objects.nonNull(message.getMobile()) && !message.getMobile().isEmpty());

    private final Predicate<Message> isValidMobile = message ->
            Objects.nonNull(message.getMobile()) && !message.getMobile().isEmpty();

    private final Predicate<Message> isValidMail = message ->
            Objects.nonNull(message.getMail()) && !message.getMail().isEmpty();

    public static final Predicate<String> isValidFormatMail = email ->
            Pattern.compile("^(([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+)+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$")
                    .matcher(email).matches();
}
