package co.com.bancolombia.binstash.redis.client;


import co.com.bancolombia.binstash.InvalidKeyException;
import io.lettuce.core.RedisClient;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

public class RedisStash {

    private static final String ERROR_KEY_MSG = "Caching key cannot be null";

    private final RedisReactiveCommands<String, String> redisReactiveCommands;
    private final int expireAfter;

    private RedisStash(RedisReactiveCommands<String, String> redisReactiveCommands,
                       int expireAfter) {
        this.redisReactiveCommands = redisReactiveCommands;
        this.expireAfter = expireAfter;
    }

    public Mono<String> save(String key, String value) {
        if (StringUtils.isAnyBlank(key, value)) {
            return Mono.error(new InvalidKeyException(ERROR_KEY_MSG));
        } else {
            return redisReactiveCommands.set(key, value, SetArgs.Builder.ex(this.expireAfter))
                    .map(r -> value);
        }
    }

    public Mono<String> get(String key) {
        if (StringUtils.isBlank(key)) {
            return Mono.error(new InvalidKeyException(ERROR_KEY_MSG));
        } else {
            return redisReactiveCommands.get(key);
        }
    }


    public static final class Builder {
        private static final int PORTREDIS = 6379;
        private static final int EXPIREAFTERTIMEINSECONDS = 300;
        private static final int ZERODATABASE = 0;
        private String host;
        private int port = PORTREDIS ;
        private int database = ZERODATABASE;
        private String password;
        private int expireAfter = EXPIREAFTERTIMEINSECONDS;

        public Builder expireAfter(int seconds) {
            this.expireAfter = seconds;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder dataBaseNumber(int db) {
            this.database = db;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        private String buildUrl() {
            var buffer = new StringBuilder();
            buffer.append("redis://");
            if (StringUtils.isNotBlank(this.password)) {
                buffer.append(this.password);
                buffer.append("@");
            }
            if (StringUtils.isNotBlank(this.host)) {
                buffer.append(this.host);
            } else {
                buffer.append("localhost");
            }
            buffer.append(":");
            buffer.append(this.port);
            if (this.database > 0) {
                buffer.append("/");
                buffer.append(this.database);
            }
            return buffer.toString();
        }

        public RedisStash build() {
            var redisClient =
                    RedisClient.create(this.buildUrl());
            RedisReactiveCommands<String, String> commands = redisClient.connect().reactive();
            return new RedisStash(commands, this.expireAfter);
        }
    }
}
