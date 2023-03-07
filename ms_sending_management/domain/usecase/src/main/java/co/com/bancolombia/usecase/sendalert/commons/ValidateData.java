package co.com.bancolombia.usecase.sendalert.commons;

import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.message.Message;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;


@UtilityClass
public class ValidateData {

    private static final String PATTERN = "^\\w++([-._+&]\\w++)*+@\\w++([.]\\w++)++$";
    private static final String PATTERN_PARAMETER = "<C\\d++>";
    public static final Predicate<Message> isValidMobile = message ->
            isNotEmpty(message.getPhone()) && message.getPhone().length() > 7;

    public static final Predicate<Message> isValidMailFormat = message ->
            isNotEmpty(message.getMail()) && Pattern.compile(PATTERN).matcher(message.getMail()).matches();

    public static final Predicate<Alert> containParameter = alert ->
            !alert.getMessage().isEmpty() || !Pattern.compile(PATTERN_PARAMETER).matcher(alert.getMessage()).find();

    public static boolean isNotEmpty(String str) {
        return !str.isEmpty();
    }

    public static final Predicate<Message> validateClient = message ->
            Objects.nonNull(message.getDocumentNumber()) && Objects.nonNull(message.getDocumentType());

}
