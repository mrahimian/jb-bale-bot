package ir.jibit.directdebit.gateway.balejbbot;

import ir.jibit.directdebit.gateway.balejbbot.bot.StudentsBot;
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

    private final StudentsBot studentsBot;

    public InitConfig(StudentsBot studentsBot) {
        this.studentsBot = studentsBot;
    }


    @Bean
    TelegramBotsLongPollingApplication init() throws TelegramApiException {
        TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
        botsApplication.registerBot(botToken, () -> new TelegramUrl("https", host, port),
                new DefaultGetUpdatesGenerator(), studentsBot);

        System.out.println("MyBot successfully started!");
        return botsApplication;
    }
}
