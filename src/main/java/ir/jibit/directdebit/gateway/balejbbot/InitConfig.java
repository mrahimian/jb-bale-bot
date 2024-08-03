package ir.jibit.directdebit.gateway.balejbbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.DefaultGetUpdatesGenerator;
import org.telegram.telegrambots.meta.TelegramUrl;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
public class InitConfig {

    @Value("${bot.bale.students.token}")
    String botToken;

    @Value("${bot.bale.students.host}")
    String host;

    @Value("${bot.bale.students.port}")
    int port;


    @Bean
    TelegramBotsLongPollingApplication init() throws TelegramApiException {
        TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
        botsApplication.registerBot(botToken, () -> new TelegramUrl("https", host, port),
                new DefaultGetUpdatesGenerator(), new StudentsBot(botToken, host, port));

        System.out.println("MyAmazingBot successfully started!");
        return botsApplication;
    }
}
