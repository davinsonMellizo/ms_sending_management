package co.com.bancolombia.events.handlers;

import co.com.bancolombia.events.dto.FiltersDTO;
import co.com.bancolombia.model.log.Log;
import co.com.bancolombia.usecase.log.LogUseCase;
import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.impl.config.annotations.EnableCommandListeners;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@EnableCommandListeners
public class CommandsHandler {

    private final LogUseCase useCase;

    public Mono<Void> saveLog(Command<Log> command) {
        return useCase.saveLog(command.getData());
    }

    public Mono<Void> retrieveLogs(Command<FiltersDTO> command) {
        return Mono.just(command.getData())
                .flatMap(FiltersDTO::toModel)
                .map(useCase::retrieveLogsS3)
                .thenEmpty(Mono.empty());
    }

}
