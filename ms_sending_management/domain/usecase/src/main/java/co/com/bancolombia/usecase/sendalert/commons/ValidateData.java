package co.com.bancolombia.usecase.sendalert.commons;

import co.com.bancolombia.model.message.Message;
import lombok.experimental.UtilityClass;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@UtilityClass
public class ValidateData {

    private static final String PATTERN = "^\\w++([-._+&]\\w++)*+@\\w++([.]\\w++)++$";

    public static final Predicate<Message> isValidMobile = message -> isNotEmpty(message.getPhone());

    public static final Predicate<Message> isValidMailFormat = message ->
            isNotEmpty(message.getMail()) && Pattern.compile(PATTERN).matcher(message.getMail()).matches();

    public static boolean isNotEmpty(String str) {
        return !str.isEmpty();
    }

}
