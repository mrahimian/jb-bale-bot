package ir.jibit.directdebit.gateway.balejbbot.bot;

import com.github.benmanes.caffeine.cache.Cache;
import ir.jibit.directdebit.gateway.balejbbot.config.CacheConfig;
import ir.jibit.directdebit.gateway.balejbbot.controller.AdminController;
import ir.jibit.directdebit.gateway.balejbbot.controller.CommonController;
import ir.jibit.directdebit.gateway.balejbbot.exceptions.BotException;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static ir.jibit.directdebit.gateway.balejbbot.bot.State.*;
import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.INVALID_INPUT;
import static ir.jibit.directdebit.gateway.balejbbot.exceptions.Error.SERVER_ERROR;

public class AdminsBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final CommonController commonController;
    private final AdminController adminController;
    private final Cache<String, CacheConfig.StateObject> states;

    public AdminsBot(TelegramClient telegramClient, CommonController commonController, AdminController adminController,
                     Cache states) {

        this.telegramClient = telegramClient;
        this.adminController = adminController;
        this.states = states;
        this.commonController = commonController;
    }

    @Override
    public void consume(Update update) {
        SendMessage message = null;
        if (update.hasMessage()) {
            var chatId = update.getMessage().getChatId();
            var messageText = update.getMessage().getText();
            try {
                if (update.getMessage().hasText() && messageText.startsWith("/start")) {
                    var credentials = messageText.split(" ");
                    var username = credentials[2];
                    var password = credentials[4];
                    var msg = commonController.login(String.valueOf(chatId), username, password, false);

                    message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(msg)
                            .replyMarkup(setKeyboard())
                            .build();
                } else if (update.getMessage().hasText() && messageText.contains("اطلاعات متربیان")) {
                    var msg = adminController.getStudents(String.valueOf(chatId));
                    message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(msg.toString())
                            .replyMarkup(setKeyboard())
                            .build();
                } else if (update.getMessage().hasText() && messageText.contains("امتیاز مثبت")) {
                    states.put(String.valueOf(chatId), new CacheConfig.StateObject(SCORE_INCREASE));
                    message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text("شناسه متربیانی که قصد امتیاز دادن به آنها دارید را وارد کنید و سپس یک * گذاشته و امتیاز را وارد نمایید.\nشناسه متربیان را با کاراکتر dash(-) از هم جدا کنید.\nمثال : 100-101-102 * 3 \nبه این معنی که متربیان با شناسه ۱۰۰ و ۱۰۱ و ۱۰۲ سه امتیاز دریافت کنند.")
                            .replyMarkup(setKeyboard())
                            .build();
                } else if (update.getMessage().hasText() && messageText.contains("امتیاز منفی")) {
                    states.put(String.valueOf(chatId), new CacheConfig.StateObject(SCORE_DECREASE));
                    message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text("شناسه متربیانی که قصد کم کردن امتیاز از آنها دارید را وارد کنید و سپس یک * گذاشته و امتیاز را وارد نمایید.\nشناسه متربیان را با کاراکتر dash(-) از هم جدا کنید.\nمثال : 100-101-102 * 3 \nبه این معنی که متربیان با شناسه ۱۰۰ و ۱۰۱ و ۱۰۲ سه امتیاز منفی دریافت کنند.")
                            .replyMarkup(setKeyboard())
                            .build();
                } else if (update.getMessage().hasText() && messageText.contains("غیر فعال کردن کمد")) {
                    var msg = adminController.disableGiftsTime(String.valueOf(chatId));
                    message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(msg)
                            .replyMarkup(setKeyboard())
                            .build();
                } else if (update.getMessage().hasText() && messageText.contains("فعال کردن کمد")) {
                    var msg = adminController.enableGiftsTime(String.valueOf(chatId));
                    message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(msg)
                            .replyMarkup(setKeyboard())
                            .build();
                } else if (update.getMessage().hasText() && messageText.contains("کمد جوایز")) {
                    var msg = commonController.getAwards(String.valueOf(chatId), false);
                    message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(msg)
                            .replyMarkup(setKeyboard())
                            .build();
                } else if (update.getMessage().hasText() && messageText.contains("بارگذاری لیست متربیان")) {
                    states.put(String.valueOf(chatId), new CacheConfig.StateObject(STUDENT_INSERTION));
                    message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text("لطفا فایل اکسل را وارد نمایید.")
                            .replyMarkup(setKeyboard())
                            .build();
                } else if (update.getMessage().hasText() && messageText.contains("بارگذاری لیست مربیان")) {
                    states.put(String.valueOf(chatId), new CacheConfig.StateObject(ADMIN_INSERTION));
                    message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text("لطفا فایل اکسل را وارد نمایید.")
                            .replyMarkup(setKeyboard())
                            .build();
                } else if (update.getMessage().hasText() && messageText.contains("بارگذاری لیست جوایز")) {
                    states.put(String.valueOf(chatId), new CacheConfig.StateObject(AWARD_INSERTION));
                    message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text("لطفا فایل اکسل را وارد نمایید.")
                            .replyMarkup(setKeyboard())
                            .build();
                } else {
                    if (states.asMap().containsKey(String.valueOf(chatId))) {
                        switch (states.getIfPresent(String.valueOf(chatId)).getState()) {
                            case SCORE_INCREASE -> {
                                var msg = handleScoreUpdate(String.valueOf(chatId), messageText, true);
                                message = SendMessage
                                        .builder()
                                        .chatId(chatId)
                                        .text(msg)
                                        .replyMarkup(setKeyboard())
                                        .build();
                            }
                            case SCORE_DECREASE -> {
                                var msg = handleScoreUpdate(String.valueOf(chatId), messageText, false);
                                message = SendMessage
                                        .builder()
                                        .chatId(chatId)
                                        .text(msg)
                                        .replyMarkup(setKeyboard())
                                        .build();
                            }
                            case STUDENT_INSERTION -> {
                                var document = update.getMessage().getDocument();
                                var msg = commonController.insertStudents(String.valueOf(chatId), document.getFileId());
                                message = SendMessage
                                        .builder()
                                        .chatId(chatId)
                                        .text(msg)
                                        .replyMarkup(setKeyboard())
                                        .build();
                            }
                            case ADMIN_INSERTION -> {
                                var document = update.getMessage().getDocument();
                                var msg = commonController.insertAdmins(String.valueOf(chatId), document.getFileId());
                                message = SendMessage
                                        .builder()
                                        .chatId(chatId)
                                        .text(msg)
                                        .replyMarkup(setKeyboard())
                                        .build();
                            }
                            case AWARD_INSERTION -> {
                                var document = update.getMessage().getDocument();
                                var msg = commonController.insertAwards(String.valueOf(chatId), document.getFileId());
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
                }

            } catch (BotException e) {
                message = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(e.getError() != null ? e.getError().getMessage() : e.getMessage())
                        .replyMarkup(setKeyboard())
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
                message = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(SERVER_ERROR.getMessage())
                        .replyMarkup(setKeyboard())
                        .build();
            }
            try {
                if (message == null) {
                    message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text("منوی اصلی ↪\uFE0F")
                            .replyMarkup(setKeyboard())
                            .build();
                }

                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private String handleScoreUpdate(String chatId, String messageText, boolean increase) {
        messageText = toEnglishNumbers(messageText.replace(" ", ""));
        var params = messageText.split("/*");
        if (params.length != 2) {
            throw new BotException(INVALID_INPUT);
        }

        var ids = params[0].split("-");
        var score = params[1];
        if (!StringUtils.isNumeric(score)) {
            throw new BotException(INVALID_INPUT);
        }

        return increase ? adminController.increaseStudentScore(chatId, ids, Integer.parseInt(score)) :
                adminController.decreaseStudentScore(chatId, ids, Integer.parseInt(score));
    }

    private ReplyKeyboardMarkup setKeyboard() {

        List<KeyboardRow> keyboard = new ArrayList<>();

        var row1 = new KeyboardRow();
        row1.add(new KeyboardButton("اطلاعات متربیان ℹ\uFE0F"));
        row1.add(new KeyboardButton("کمد جوایز \uD83C\uDF81"));

        var row2 = new KeyboardRow();
        row2.add(new KeyboardButton("امتیاز مثبت ✔\uFE0F"));
        row2.add(new KeyboardButton("امتیاز منفی ✖\uFE0F"));

        var row3 = new KeyboardRow();
        row3.add(new KeyboardButton("فعال کردن کمد جوایز \uD83E\uDD29"));

        var row4 = new KeyboardRow();
        row4.add(new KeyboardButton("غیر فعال کردن کمد جوایز ☹\uFE0F"));

        var row5 = new KeyboardRow();
        row5.add(new KeyboardButton("بارگذاری لیست متربیان \uD83E\uDDD1\u200D\uD83D\uDCBC"));

        var row6 = new KeyboardRow();
        row6.add(new KeyboardButton("بارگذاری لیست مربیان \uD83D\uDC68\u200D\uD83D\uDCBC"));

        var row7 = new KeyboardRow();
        row7.add(new KeyboardButton("بارگذاری لیست جوایز \uD83C\uDFC6"));


        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboard.add(row5);
        keyboard.add(row6);
        keyboard.add(row7);

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