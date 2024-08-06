package ir.jibit.directdebit.gateway.balejbbot.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import ir.jibit.directdebit.gateway.balejbbot.bot.State;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheConfig {
    @Value("${cache.expire_after_seconds}")
    private int cacheExpireAfter;

    @Bean
    public Cache<String, StateObject> captchaCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(cacheExpireAfter))
                .recordStats()
                .build();
    }

    public static class StateObject {
        State state;
        Object object;

        public StateObject(State state) {
            this.state = state;
        }

        public StateObject(State state, Object object) {
            this.state = state;
            this.object = object;
        }
    }
}
