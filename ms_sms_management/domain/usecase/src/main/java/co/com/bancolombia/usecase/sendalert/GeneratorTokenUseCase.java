package co.com.bancolombia.usecase.sendalert;

import co.com.bancolombia.binstash.api.ObjectCache;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.SMSInalambria;
import co.com.bancolombia.model.message.SMSMasiv;
import co.com.bancolombia.model.message.gateways.InalambriaGateway;
import co.com.bancolombia.model.message.gateways.MasivianGateway;
import co.com.bancolombia.model.token.SecretGateway;
import co.com.bancolombia.model.token.Token;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class GeneratorTokenUseCase implements Serializable {
    private final transient ObjectCache<ArrayList> token;
    private final transient SecretGateway secretGateway;
    private final transient InalambriaGateway inalambriaGateway;
    private final transient MasivianGateway masivianGateway;

    public Mono<SMSInalambria> getTokenINA(SMSInalambria smsInalambria, Alert alert) {
        return  token.get(alert.getPriority().concat(alert.getProvider()), ArrayList.class)
                .filter(lisToken->!lisToken.isEmpty())
                .switchIfEmpty(getTokenByProviderINA(alert.getPriority().concat(alert.getProvider())))
                .switchIfEmpty(Mono.error(new RuntimeException("Not Token Found")))
                .map(tokens->tokens.get(0).toString())
                .map(tokenInalambria1 -> Map.<String, Object>of("Authorization","Bearer "+tokenInalambria1))
                .map(headers-> setTokenINA(smsInalambria, headers));
    }
    public Mono<SMSMasiv> getTokenMAS(SMSMasiv smsMasiv,Alert alert){
        return token.get(alert.getPriority().concat(alert.getProvider()),ArrayList.class)
                .filter(lisToken->!lisToken.isEmpty())
                .switchIfEmpty(getTokenByProviderMAS(alert.getPriority().concat(alert.getProvider())))
                .switchIfEmpty(Mono.error(new RuntimeException("Not Token Found")))
                .map(tokens->tokens.get(0).toString())
                .map(tokenMas->Map.<String, Object>of("Authorization","Bearer "+tokenMas))
                .map(headers-> setTokenMAS(smsMasiv,headers));
    }

    public Mono<Void> deleteToken(String usedToken, Alert alert) {
        return token.get(alert.getPriority().concat(alert.getProvider()), ArrayList.class)
                .flatMap(lisTokens->getArrayListArrayListFunction(usedToken,lisTokens))
                .flatMap(data->token.save(alert.getPriority().concat(alert.getProvider()),data))
                .then(Mono.empty());
    }

    private SMSInalambria setTokenINA(SMSInalambria smsInalambria, Map<String, Object> headers) {
        smsInalambria.setHeaders(headers);
        return smsInalambria;
    }
    private SMSMasiv setTokenMAS(SMSMasiv smsMasiv,Map<String,Object> headers){
        smsMasiv.setHeaders(headers);
        return smsMasiv;
    }

    private Mono<ArrayList> getTokenByProviderINA(String key) {
        return secretGateway.getSecretName(key)
                .flatMap(inalambriaGateway::getToken)
                .flatMap(token ->saveTokenCache(token,key));
    }

    private Mono<ArrayList> getTokenByProviderMAS(String key){
        return secretGateway.getSecretName(key)
                .flatMap(masivianGateway::getToken)
                .flatMap(token->saveTokenCache(token,key));
    }


    private Mono<ArrayList> saveTokenCache(Token token, String key){
        return this.token.get(key, ArrayList.class)
                .map(listTokens->{listTokens.add(0,token.getAccessToken()); return listTokens;})
                .switchIfEmpty(Mono.just(new ArrayList<>(List.of(token.getAccessToken()))))
                .flatMap(listTokens-> this.token.save(key,listTokens));
    }
    private Mono<ArrayList> getArrayListArrayListFunction(String token, ArrayList array) {
        return Mono.just(array.indexOf(token))
                .map(index -> array.remove(token))
                .thenReturn(array);
    }


}
