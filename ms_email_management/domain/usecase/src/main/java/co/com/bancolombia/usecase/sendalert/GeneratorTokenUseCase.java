package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.binstash.api.ObjectCache;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Mail;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.token.DynamoGateway;
import co.com.bancolombia.model.token.SecretGateway;
import co.com.bancolombia.model.token.Token;
import co.com.bancolombia.usecase.log.LogUseCase;
import co.com.bancolombia.usecase.log.ValidationLogUtil;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.usecase.sendalert.commons.Medium.EMAIL;

@RequiredArgsConstructor
public class GeneratorTokenUseCase {
    private final ObjectCache<ArrayList> token;
    private final SecretGateway secretGateway;
    private final DynamoGateway dynamoGateway;
    private final MasivianGateway masivianGateway;
    private final LogUseCase logUseCase;

    public Mono<Mail> getToken(Mail mail, Alert alert) {

        return  Mono.just(mail.getNameToken())
                .flatMap(nameToken->token.get(nameToken, ArrayList.class))
                .filter(lisToken->!lisToken.isEmpty())
                .switchIfEmpty(getTokenByProvider(mail.getFrom().split("@")[1].split(">")[0], mail.getNameToken()))
                .map(tokens->tokens.get(0).toString())
                .map(token1->Map.of("Authorization","Bearer "+token1))
                .map(headers->setToken(mail,headers))
                .onErrorResume(e -> ValidationLogUtil.valideitorSendLog(alert,  EMAIL,  Response.builder().code(1)
                        .description(e.getMessage()).build(), logUseCase,null));
    }
    public Mono<String> getNameToken(Alert alert){
        return dynamoGateway.getTokenName(alert.getFrom().split("@")[1].split(">")[0])
                .map(nameToken-> nameToken.getSecretName());
    }

    private Mail setToken(Mail mail, Map<String, String> headers) {
        mail.setHeaders(headers);
        return mail;
    }
    private Mono<ArrayList> getTokenByProvider(String key,String tokenName) {
        return secretGateway.getSecretName(key)
                .flatMap(masivianGateway::getToken)
                .flatMap(token1->saveTokenCache(token1,tokenName));
    }
    private Mono<ArrayList> saveTokenCache(Token token, String nameToken){
        return this.token.get(nameToken, ArrayList.class)
                .map(listTokens->{listTokens.add(0,token.getAccessToken()); return listTokens;})
                .switchIfEmpty(Mono.just(new ArrayList<>(List.of(token.getAccessToken()))))
                .flatMap(listTokens-> this.token.save(nameToken,listTokens));
    }

    private Mono<ArrayList> getArrayListArrayListFunction(String token, ArrayList array) {
        return Mono.just(array.indexOf(token))
                .map(index -> array.remove(token))
                .thenReturn(array);
    }
    public Mono<Void> deleteToken(String usedToken,Alert alert){
        return getNameToken(alert)
                .flatMap(nameToken->token.get(nameToken,ArrayList.class))
                .flatMap(listTokens->getArrayListArrayListFunction(usedToken,listTokens))
                .zipWith(getNameToken(alert))
                .flatMap(data->token.save(data.getT2(), data.getT1()))
                .then(Mono.empty());
    }

}
