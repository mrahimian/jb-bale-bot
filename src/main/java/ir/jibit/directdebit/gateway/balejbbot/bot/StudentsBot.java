package ir.jibit.directdebit.gateway.balejbbot.bot;

import com.github.benmanes.caffeine.cache.Cache;
import ir.jibit.directdebit.gateway.balejbbot.config.CacheConfig;
import ir.jibit.directdebit.gateway.balejbbot.controller.CommonController;
import ir.jibit.directdebit.gateway.balejbbot.controller.StudentController;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.TelegramUrl;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

import static ir.jibit.directdebit.gateway.balejbbot.bot.State.REQUEST_FOR_AWARD;
import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.SERVER_ERROR;

@Component
public class StudentsBot implements LongPollingSingleThreadUpdateConsumer {
    @Value("${bot.bale.students.token}")
    String botToken;

    @Value("${bot.bale.students.host}")
    String host;

    @Value("${bot.bale.students.port}")
    int port;

    private final TelegramClient telegramClient;
    private final CommonController commonController;
    private final StudentController studentController;
    private final Cache<String, CacheConfig.StateObject> states;

    public StudentsBot(CommonController commonController, StudentController studentController, Cache states) {
        this.states = states;
        this.telegramClient = new OkHttpTelegramClient(botToken, new TelegramUrl("https", host, port));
        this.commonController = commonController;
        this.studentController = studentController;
    }

    @Override
    public void consume(Update update) {
        SendMessage message = null;
        if (update.hasMessage()) {
            var chatId = update.getMessage().getChatId();
            var messageText = update.getMessage().getText();
            try {
                if (messageText.startsWith("/start")) {
                    var credentials = messageText.split(" ");
                    var username = credentials[2];
                    var password = credentials[4];
                    var msg = commonController.login(String.valueOf(chatId), username, password, true);

                    message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(msg)
                            .replyMarkup(setKeyboard())
                            .build();
                }
                if (messageText.contains("دیدن اطلاعات خودم")) {
                    var msg = studentController.getInfo(String.valueOf(chatId));
                    message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(msg)
                            .build();
                }

                if (messageText.contains("امتیاز من")) {
                    var msg = studentController.getScore(String.valueOf(chatId));
                    message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(msg)
                            .build();
                }

                if (messageText.contains("کمد جوایز")) {
                    var msg = studentController.getAwards(String.valueOf(chatId));
                    message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(msg)
                            .build();
                }

                if (messageText.contains("درخواست جایزه")) {
                    if (studentController.isGiftTimeEnable(String.valueOf(chatId))) {
                        message = SendMessage
                                .builder()
                                .chatId(chatId)
                                .text("لطفا کد جایزه مورد نظر را وارد کنید")
                                .build();
                        states.put(String.valueOf(chatId), new CacheConfig.StateObject(REQUEST_FOR_AWARD));
                    } else {
                        message = SendMessage
                                .builder()
                                .chatId(chatId)
                                .text("کمد جوایز در حال حاضر فعال نمی‌باشد. \uD83D\uDE22")
                                .build();
                    }
                }

            } catch (BotException e) {
                message = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(e.getError().getMessage())
                        .build();
            } catch (Exception e) {
                message = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(SERVER_ERROR.getMessage())
                        .build();
            }
            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

//    message = SendMessage // Create a message object
//                        .builder()
//                        .chatId(chatId)
//                        .text(messageText)
//                        .replyMarkup(InlineKeyboardMarkup
//                                .builder()
//                                .keyboardRow(
//                                        new InlineKeyboardRow(InlineKeyboardButton
//                                                .builder()
//                                                .text("Update message text")
//                                                .callbackData("update_msg_text")
//                                                .build()
//                                        )
//                                )
//                                .build())
//                        .build();


    private ReplyKeyboardMarkup setKeyboard() {

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("دیدن اطلاعات خودم ℹ\uFE0F"));
        row1.add(new KeyboardButton("امتیاز من \uD83C\uDFC6"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("کمد جوایز \uD83C\uDF81"));
        row2.add(new KeyboardButton("درخواست جایزه ⚽\uFE0F\uD83C\uDFC0"));

        keyboard.add(row1);
        keyboard.add(row2);

        return ReplyKeyboardMarkup
                .builder()
                .keyboard(keyboard)
                .build();
    }
}