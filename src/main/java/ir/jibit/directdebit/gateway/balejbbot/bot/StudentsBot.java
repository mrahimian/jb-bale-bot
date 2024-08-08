package ir.jibit.directdebit.gateway.balejbbot.bot;

import com.github.benmanes.caffeine.cache.Cache;
import ir.jibit.directdebit.gateway.balejbbot.config.CacheConfig;
import ir.jibit.directdebit.gateway.balejbbot.controller.CommonController;
import ir.jibit.directdebit.gateway.balejbbot.controller.StudentController;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
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
import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.INVALID_INPUT;
import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.SERVER_ERROR;

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

    public StudentsBot(TelegramClient telegramClient, CommonController commonController, StudentController studentController,
                       Cache states) {

        this.telegramClient = telegramClient;
        this.states = states;
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
                if (update.getMessage().hasText() && messageText.contains("دیدن اطلاعات خودم")) {
                    var msg = studentController.getInfo(String.valueOf(chatId));
                    message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(msg)
                            .replyMarkup(setKeyboard())
                            .build();
                }

                if (update.getMessage().hasText() && messageText.contains("امتیاز من")) {
                    var msg = studentController.getScore(String.valueOf(chatId));
                    message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(msg)
                            .replyMarkup(setKeyboard())
                            .build();
                }

                if (update.getMessage().hasText() && messageText.contains("کمد جوایز")) {
                    var msg = commonController.getAwards(String.valueOf(chatId), true);
                    message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(msg)
                            .replyMarkup(setKeyboard())
                            .build();
                }

                if (update.getMessage().hasText() && messageText.contains("درخواست جایزه")) {
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
                                .replyMarkup(setKeyboard())
                                .build();
                    }
                }

                if (states.asMap().containsKey(String.valueOf(chatId))) {
                    switch (states.getIfPresent(String.valueOf(chatId)).getState()) {
                        case REQUEST_FOR_AWARD -> {
                            if (!StringUtils.isNumeric(toEnglishNumbers(messageText))) {
                                throw new BotException(INVALID_INPUT);
                            }

                            var msg = studentController.requestForAward(String.valueOf(chatId), Integer.parseInt(messageText));
                            message = SendMessage
                                    .builder()
                                    .chatId(chatId)
                                    .text(msg)
                                    .replyMarkup(setKeyboard())
                                    .build();
                        }
                        default -> throw new Exception();
                    }
                }

            } catch (BotException e) {
                message = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(e.getError() != null ? e.getError().getMessage() : e.getMessage())
                        .replyMarkup(setKeyboard())
                        .build();
            } catch (Exception e) {
                message = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(SERVER_ERROR.getMessage())
                        .replyMarkup(setKeyboard())
                        .build();
            }
            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

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

    private String toEnglishNumbers(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.replace('۰', '0')
                .replace('۱', '1')
                .replace('۲', '2')
                .replace('۳', '3')
                .replace('۴', '4')
                .replace('۵', '5')
                .replace('۶', '6')
                .replace('۷', '7')
                .replace('۸', '8')
                .replace('۹', '9');
    }
}