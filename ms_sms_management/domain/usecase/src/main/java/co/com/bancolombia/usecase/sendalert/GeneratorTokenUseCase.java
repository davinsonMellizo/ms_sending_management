package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.binstash.api.ObjectCache;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.SMSInalambria;
import co.com.bancolombia.model.message.SMSInfobip;
import co.com.bancolombia.model.message.SMSMasiv;
import co.com.bancolombia.model.message.gateways.InalambriaGateway;
import co.com.bancolombia.model.message.gateways.InfobipGateway;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.token.SecretGateway;
import co.com.bancolombia.model.token.Token;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.SECRET_NAME_NOT_FOUND;


@RequiredArgsConstructor
public class GeneratorTokenUseCase implements Serializable {
    private final transient ObjectCache<ArrayList> redisGateway;
    private final transient SecretGateway secretGateway;
    private final transient InalambriaGateway inalambriaGateway;
    private final transient MasivianGateway masivianGateway;
    private final transient InfobipGateway infobipGateway;

    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    public Mono<SMSInalambria> getTokenINA(SMSInalambria smsInalambria, Alert alert) {
        return redisGateway.get(alert.getPriority().concat(alert.getProvider()), ArrayList.class)
                .filter(lisToken -> !lisToken.isEmpty())
                .switchIfEmpty(getTokenByProviderINA(alert.getPriority().concat(alert.getProvider())))
                .map(tokens -> tokens.get(0).toString())
                .map(tokenInalambria1 -> Map.of(AUTHORIZATION, BEARER + tokenInalambria1))
                .map(headers -> setTokenINA(smsInalambria, headers));
    }

    public Mono<SMSMasiv> getTokenMAS(SMSMasiv smsMasiv, Alert alert) {
        return redisGateway.get(alert.getPriority().concat(alert.getProvider()), ArrayList.class)
                .filter(lisToken -> !lisToken.isEmpty())
                .switchIfEmpty(getTokenByProviderMAS(alert.getPriority().concat(alert.getProvider())))
                .map(tokens -> tokens.get(0).toString())
                .map(tokenMas -> Map.of(AUTHORIZATION, BEARER + tokenMas))
                .map(headers -> setTokenMAS(smsMasiv, headers));
    }

    public Mono<SMSInfobip> getTokenInf(SMSInfobip smsInfobip, Alert alert){
        return redisGateway.get(alert.getPriority().concat(alert.getProvider()),ArrayList.class)
                .filter(lisToken->!lisToken.isEmpty())
                .switchIfEmpty(getTokenByProviderInf(alert.getPriority().concat(alert.getProvider())))
                .map(tokens->tokens.get(0).toString())
                .map(tokenInf->Map.of(AUTHORIZATION,BEARER + tokenInf))
                .map(headers-> setTokenINF(smsInfobip,headers));
    }

    public Mono<Void> deleteToken(String usedToken, Alert alert) {
        return redisGateway.get(alert.getPriority().concat(alert.getProvider()), ArrayList.class)
                .flatMap(lisTokens -> getArrayListArrayListFunction(usedToken, lisTokens))
                .flatMap(data -> redisGateway.save(alert.getPriority().concat(alert.getProvider()),
                        (ArrayList<String>) data))
                .then(Mono.empty());
    }

    private SMSInalambria setTokenINA(SMSInalambria smsInalambria, Map<String, String> headers) {
        smsInalambria.setHeaders(headers);
        return smsInalambria;
    }

    private SMSMasiv setTokenMAS(SMSMasiv smsMasiv, Map<String, String> headers) {
        smsMasiv.setHeaders(headers);
        return smsMasiv;
    }

    private SMSInfobip setTokenINF(SMSInfobip smsInfobip, Map<String,String> headers){
        smsInfobip.setHeaders(headers);
        return smsInfobip;
    }

    private Mono<ArrayList> getTokenByProviderINA(String key) {
        return secretGateway.getSecretName(key)
                .switchIfEmpty(Mono.error(new BusinessException(SECRET_NAME_NOT_FOUND)))
                .flatMap(inalambriaGateway::getToken)
                .flatMap(token -> saveTokenCache(token, key));
    }

    private Mono<ArrayList> getTokenByProviderMAS(String key) {
        return secretGateway.getSecretName(key)
                .switchIfEmpty(Mono.error(new BusinessException(SECRET_NAME_NOT_FOUND)))
                .flatMap(masivianGateway::getToken)
                .flatMap(token -> saveTokenCache(token, key));
    }

    private Mono<ArrayList> getTokenByProviderInf(String key){
        return secretGateway.getSecretName(key)
                .switchIfEmpty(Mono.error(new BusinessException(SECRET_NAME_NOT_FOUND)))
                .flatMap(infobipGateway::getToken)
                .flatMap(token->saveTokenCache(token,key));
    }


    private Mono<ArrayList> saveTokenCache(Token token, String key) {
        return this.redisGateway.get(key, ArrayList.class)
                .map(listTokens -> {
                    listTokens.add(0, token.getAccessToken());
                    return listTokens;
                })
                .switchIfEmpty(Mono.just(new ArrayList<>(List.of(token.getAccessToken()))))
                .flatMap(listTokens -> this.redisGateway.save(key, listTokens));
    }

    private Mono<ArrayList<String>> getArrayListArrayListFunction(String token, ArrayList<String> array) {
        return Mono.just(array.indexOf(token))
                .map(index -> array.remove(token))
                .thenReturn(array);
    }


}
