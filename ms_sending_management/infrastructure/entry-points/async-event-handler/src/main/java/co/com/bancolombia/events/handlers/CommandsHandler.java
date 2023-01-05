package co.com.bancolombia.events.handlers;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.impl.config.annotations.EnableCommandListeners;
import reactor.core.publisher.Mono;
import reactor.function.Function3;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;


@AllArgsConstructor
@EnableCommandListeners
public class CommandsHandler {


    public Mono<Void> saveClient(Command<String> command){
        System.out.println("este el men"+ command.getData());
        return Mono.empty();
    }


}
