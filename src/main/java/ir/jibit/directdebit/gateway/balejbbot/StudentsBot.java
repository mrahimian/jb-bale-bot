package ir.jibit.directdebit.gateway.balejbbot;

import ir.jibit.directdebit.gateway.balejbbot.controller.AdminController;
import ir.jibit.directdebit.gateway.balejbbot.controller.CommonController;
import ir.jibit.directdebit.gateway.balejbbot.controller.StudentController;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.TelegramUrl;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

public class StudentsBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final CommonController commonController;
    private final StudentController studentController;

    public StudentsBot(String token, String host, int port, CommonController commonController,
                       StudentController studentController) {

        this.telegramClient = new OkHttpTelegramClient(token, new TelegramUrl("https", host, port));
        this.commonController = commonController;
        this.studentController = studentController;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            SendMessage message = null;
            var chatId = update.getMessage().getChatId();
            var messageText = update.getMessage().getText();
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

            }


//            SendMessage message = SendMessage
//                    .builder()
//                    .chatId(chatId)
//                    .text(messageText)
//                    .build();
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