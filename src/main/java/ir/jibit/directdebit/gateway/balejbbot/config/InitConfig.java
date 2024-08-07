package ir.jibit.directdebit.gateway.balejbbot.config;

import com.github.benmanes.caffeine.cache.Cache;
import ir.jibit.directdebit.gateway.balejbbot.bot.AdminsBot;
import ir.jibit.directdebit.gateway.balejbbot.bot.StudentsBot;
import ir.jibit.directdebit.gateway.balejbbot.controller.AdminController;
import ir.jibit.directdebit.gateway.balejbbot.controller.CommonController;
import ir.jibit.directdebit.gateway.balejbbot.controller.StudentController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.DefaultGetUpdatesGenerator;
import org.telegram.telegrambots.meta.TelegramUrl;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
public class InitConfig {

    @Value("${bot.bale.students.token}")
    String studentsBotToken;

    @Value("${bot.bale.students.host}")
    String studentsBotHost;

    @Value("${bot.bale.students.port}")
    int studentsBotPort;

    @Value("${bot.bale.admins.token}")
    String adminsBotToken;

    @Value("${bot.bale.admins.host}")
    String adminsBotHost;

    @Value("${bot.bale.admins.port}")
    int adminsBotPort;


    @Bean
    TelegramBotsLongPollingApplication init(StudentsBot studentsBot, AdminsBot adminsBot) throws TelegramApiException {
        TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
        botsApplication.registerBot(studentsBotToken, () -> new TelegramUrl("https", studentsBotHost, studentsBotPort),
                new DefaultGetUpdatesGenerator(), studentsBot);

        botsApplication.registerBot(adminsBotToken, () -> new TelegramUrl("https", adminsBotHost, adminsBotPort),
                new DefaultGetUpdatesGenerator(), adminsBot);

        System.out.println("MyBots successfully started!");
        return botsApplication;
    }


    @Bean
    StudentsBot studentsBot(CommonController commonController, StudentController studentController,
                            Cache<String, CacheConfig.StateObject> states) {
        return new StudentsBot(new OkHttpTelegramClient(studentsBotToken, new TelegramUrl("https", studentsBotHost, studentsBotPort)),
                commonController, studentController, states);

    }

    @Bean
    AdminsBot adminsBot(CommonController commonController, AdminController adminController,
                        Cache states) {
        return new AdminsBot(new OkHttpTelegramClient(adminsBotToken, new TelegramUrl("https", adminsBotHost, adminsBotPort)),
                commonController, adminController, states);
    }


}
