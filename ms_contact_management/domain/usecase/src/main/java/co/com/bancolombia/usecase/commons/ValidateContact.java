package co.com.bancolombia.usecase.commons;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.client.Enrol;
import co.com.bancolombia.model.contact.Contact;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import static co.com.bancolombia.commons.constants.ContactWay.MAIL;
import static co.com.bancolombia.commons.constants.ContactWay.SMS;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_EMAIL;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_ENVIRONMENT;
import static co.com.bancolombia.commons.enums.BusinessErrorMessage.INVALID_PHONE;

@UtilityClass
public class ValidateContact {

    private static final String PATTERN = "^\\(\\+\\d{1,4}+\\)\\d{8,12}+$";
    private static final String PATTERN_MAIL = "^\\w++([-._+&]\\w++)*+@\\w++([.]\\w++)++$";
    private static final String PATTERN_CO = "^\\(\\+57\\)\\d{10,12}+$";
    private static final String PATTERN_PREFIX_CO = "^\\(\\+57\\)";
    private static final String PATTERN_SEPARATOR = "\\)((\\d++-\\d++)|\\d++)$";
    public static final Predicate<String> validatePhoneNumberRegex = contact ->
            (!Pattern.compile(PATTERN_PREFIX_CO).matcher(contact).matches() &&
                    Pattern.compile(PATTERN).matcher(contact).matches()) ||
                    (Pattern.compile(PATTERN_CO).matcher(contact).matches());

    public static final Predicate<String> validateSeparator = contact ->
            Pattern.compile(PATTERN_SEPARATOR).matcher(contact).find();

    private Mono<String> findContactSMS(Contact pContact){
        return Mono.just(pContact.getValue())
                .filter(validateSeparator)
                .map(contact -> contact.replace("-",""))
                .filter(validatePhoneNumberRegex)
                .doOnNext(contactValue -> pContact.setValue(contactValue))
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_PHONE)));
    }
    public Mono<Enrol> validatePhone(Enrol enrol) {
        return Flux.fromIterable(enrol.getContactData())
                .filter(cnt -> SMS.equals(cnt.getContactWay()))
                .next()
                .flatMap(ValidateContact::findContactSMS)
                .map(contact -> enrol)
                .switchIfEmpty(Mono.just(enrol));
    }

    private Mono<Contact> validateEnvironment(Contact contact) {
        return Mono.just(contact)
                .filter(cnt -> !cnt.getEnvironmentType().isEmpty())
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_ENVIRONMENT)));
    }
    public Mono<Enrol> validateMail(Enrol enrol) {
        return Flux.fromIterable(enrol.getContactData())
                .filter(cnt -> MAIL.equals(cnt.getContactWay()))
                .flatMap(ValidateContact::validateEnvironment)
                .filter(cnt -> !Pattern.compile(PATTERN_MAIL).matcher(cnt.getValue()).matches())
                .next()
                .flatMap(contact -> Mono.error(new BusinessException(INVALID_EMAIL)))
                .map(o -> enrol)
                .switchIfEmpty(Mono.just(enrol));
    }

}
