package co.com.bancolombia.consumer.adapter.response.model;


import co.com.bancolombia.model.token.Token;

public interface TokenInalambriaMapper {
    Token toEntity(TokenInalambriaData tokenInalambriaData);
    TokenInalambriaData toData(Token token);
}
