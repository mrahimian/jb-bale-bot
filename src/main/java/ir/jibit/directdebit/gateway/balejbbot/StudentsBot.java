package ir.jibit.directdebit.gateway.balejbbot;

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

    public StudentsBot(String token, String host, int port) {
        this.telegramClient = new OkHttpTelegramClient(token, new TelegramUrl("https", host, port));
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            SendMessage message = null;
            var chatId = update.getMessage().getChatId();
            var messageText = update.getMessage().getText();
            if (messageText.equals("/start")){

                message = SendMessage // Create a message object
                        .builder()
                        .chatId(chatId)
                        .text(messageText)
                        .replyMarkup(setKeyboard())
                        .build();
            }
            if (messageText.equals("Button 1")){

                message = SendMessage // Create a message object
                        .builder()
                        .chatId(chatId)
                        .text(messageText)
                        .replyMarkup(InlineKeyboardMarkup
                                .builder()
                                .keyboardRow(
                                        new InlineKeyboardRow(InlineKeyboardButton
                                                .builder()
                                                .text("Update message text")
                                                .callbackData("update_msg_text")
                                                .build()
                                        )
                                )
                                .build())
                        .build();
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


    private ReplyKeyboardMarkup setKeyboard() {

        List<KeyboardRow> keyboard = new ArrayList<>();

        // First row of buttons
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Button 1"));
        row1.add(new KeyboardButton("Button 2"));

        // Second row of buttons
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Button 3"));
        row2.add(new KeyboardButton("Button 4"));

        // Add rows to the keyboard
        keyboard.add(row1);
        keyboard.add(row2);

        return ReplyKeyboardMarkup
                .builder()
                .keyboard(keyboard)
                .build();
    }
}