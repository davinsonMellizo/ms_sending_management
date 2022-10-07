package co.com.bancolombia.api.commons.util;

import co.com.bancolombia.api.services.dto.QueryLogDto;
import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Consumer;

@UtilityClass
public class ParamsUtil {


    private final static  String DOCUMENT_NUMBER= "document-number";
    private final static  String DOCUMENT_TYPE= "document-type";
    private final static  String CONTACT= "contact";
    private final static  String CONSUMER= "consumer";
    private final static  String PROVIDER= "provider";
    private final static  String START_DATE= "start-date";
    private final static  String END_DATE= "end-date";


    private static Optional<String> ofEmpty(String value) {
        return (value == null || value.isEmpty()) ? Optional.empty() : Optional.of(value);
    }
    private static Mono<LocalDateTime> ofEmptyDate(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return (value == null || value.isEmpty()) ? Mono.empty() : Mono.just(LocalDateTime.parse(value, formatter));
    }

    private static String getHeader(ServerRequest request, String header) {
        return ofEmpty(request.headers().firstHeader(header)).orElse("");
    }
    private static  Mono<QueryLogDto> getHeaderDate(ServerRequest request, String header, QueryLogDto queryLogDto,
                                                    Consumer<LocalDateTime> setDate) {
        return ofEmptyDate(request.headers().firstHeader(header))
                .switchIfEmpty(Mono.just(LocalDateTime.now().minusDays(90)))
                .doOnNext(localDateTime -> setDate.accept(localDateTime))
                .thenReturn(queryLogDto);
    }
    private static  Mono<QueryLogDto> getHeaderEndDate(ServerRequest request, String header, QueryLogDto queryLogDto,
                                                    Consumer<LocalDateTime> setDate) {
        return ofEmptyDate(request.headers().firstHeader(header))
                .doOnNext(localDateTime -> setDate.accept(localDateTime))
                .thenReturn(queryLogDto);
    }

    public static Mono<QueryLogDto> getClientHeaders(ServerRequest request) {
        return Mono.just(QueryLogDto.builder()
                .documentNumber(getHeader(request, DOCUMENT_NUMBER))
                .documentType(getHeader(request, DOCUMENT_TYPE))
                .contactValue(getHeader(request, CONTACT))
                .consumer(getHeader(request, CONSUMER))
                .provider(getHeader(request, PROVIDER))
                .referenceDate(LocalDateTime.now().minusDays(90))
                .build())
                .flatMap(queryLogDto -> getHeaderDate(request, START_DATE, queryLogDto, queryLogDto::setStartDate))
                .flatMap(queryLogDto -> getHeaderEndDate(request, END_DATE, queryLogDto, queryLogDto::setEndDate))
                .onErrorMap(e-> new TechnicalException(e, TechnicalExceptionEnum.HEADER_ERROR));
    }


}
