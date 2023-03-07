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

@UtilityClass
public class ParamsUtil {


    private static final String DOCUMENT_NUMBER = "document-number";
    private static final String DOCUMENT_TYPE = "document-type";
    private static final String CONTACT = "contact";
    private static final String CONSUMER = "consumer";
    private static final String PROVIDER = "provider";
    private static final String START_DATE = "start-date";
    private static final String END_DATE = "end-date";


    private static Optional<String> ofEmpty(String value) {
        return (value == null || value.isEmpty()) ? Optional.empty() : Optional.of(value);
    }

    private static Mono<LocalDateTime> ofEmptyDate(String value) {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        return (value == null || value.isEmpty()) ? Mono.empty() : Mono.just(LocalDateTime.parse(value, formatter));
    }

    private static String getHeader(ServerRequest request, String header) {
        return ofEmpty(request.headers().firstHeader(header)).orElse("");
    }

    private static Mono<QueryLogDto> getHeaderDate(ServerRequest request, String header, QueryLogDto queryLogDto,
                                                   Integer daysHotData) {
        return ofEmptyDate(request.headers().firstHeader(header))
                .switchIfEmpty(Mono.just(LocalDateTime.now().minusDays(daysHotData)))
                .doOnNext(localDateTime -> queryLogDto.setStartDate(localDateTime))
                .thenReturn(queryLogDto);
    }

    private static Mono<QueryLogDto> getHeaderEndDate(ServerRequest request, String header, QueryLogDto queryLogDto) {
        return ofEmptyDate(request.headers().firstHeader(header))
                .doOnNext(localDateTime -> queryLogDto.setEndDate(localDateTime))
                .thenReturn(queryLogDto);
    }

    public static Mono<QueryLogDto> getClientHeaders(ServerRequest request, Integer daysHotData) {
        return Mono.just(QueryLogDto.builder()
                        .documentNumber(getHeader(request, DOCUMENT_NUMBER))
                        .documentType(getHeader(request, DOCUMENT_TYPE))
                        .contactValue(getHeader(request, CONTACT))
                        .consumer(getHeader(request, CONSUMER))
                        .provider(getHeader(request, PROVIDER))
                        .referenceDate(LocalDateTime.now().minusDays(daysHotData))
                        .build())
                .flatMap(queryLogDto -> getHeaderDate(request, START_DATE, queryLogDto, daysHotData))
                .flatMap(queryLogDto -> getHeaderEndDate(request, END_DATE, queryLogDto))
                .onErrorMap(e -> new TechnicalException(e, TechnicalExceptionEnum.HEADER_ERROR));
    }


}
