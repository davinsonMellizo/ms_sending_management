package co.com.bancolombia.api.dto;

import reactor.core.publisher.Mono;

public abstract class DTO {
    public abstract <T> Mono<T> toModel();
}
