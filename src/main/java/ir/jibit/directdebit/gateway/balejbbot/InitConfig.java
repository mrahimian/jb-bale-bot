package ir.jibit.directdebit.gateway.balejbbot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.DefaultGetUpdatesGenerator;
import org.telegram.telegrambots.meta.TelegramUrl;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
public class InitConfig {

    @Bean
    TelegramBotsLongPollingApplication init() throws TelegramApiException {
        TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();

        // Register our bot
        String botToken = "507735893:LXuomDJL9THYsJwep3JRPJauwSM4qzSAITv3mpvf";
//        botsApplication.registerBot(botToken, new MyAmazingBot(botToken));
        botsApplication.registerBot(botToken, () -> new TelegramUrl("https", "tapi.bale.ai", 443), new DefaultGetUpdatesGenerator(), new MyAmazingBot(botToken));
        System.out.println("MyAmazingBot successfully started!");
        return botsApplication;
    }
}
