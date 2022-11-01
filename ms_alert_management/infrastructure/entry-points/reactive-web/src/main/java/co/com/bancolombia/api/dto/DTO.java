package co.com.bancolombia.api.dto;

import reactor.core.publisher.Mono;

public abstract class DTO<T> {
    public abstract Mono<T> toModel();
}
