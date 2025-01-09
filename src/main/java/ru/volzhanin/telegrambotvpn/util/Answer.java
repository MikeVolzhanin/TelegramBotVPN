package ru.volzhanin.telegrambotvpn.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Answer {
    public static SendMessage createReplyKeyboard(SendMessage message) {
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();

        row1.add(new KeyboardButton("Инструкция"));
        row2.add(new KeyboardButton("Статус подписки"));
        row3.add(new KeyboardButton("Оплата подписки"));

        // Добавляем строки в клавиатуру
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(keyboard);

        // Создание клавиатуры
        keyboardMarkup.setResizeKeyboard(true); // Уменьшить размер кнопок
        keyboardMarkup.setOneTimeKeyboard(true); // Клавиатура исчезает после нажатия

        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

    public static SendMessage instructionOptions(SendMessage message) {
        InlineKeyboardButton button1, button2, button3;
        button1 = InlineKeyboardButton.builder().text("Android").callbackData("android_clicked").build();
        button2 = InlineKeyboardButton.builder().text("IOS").callbackData("IOS_clicked").build();
        button3 = InlineKeyboardButton.builder().text("Windows").callbackData("windows_clicked").build();

        InlineKeyboardRow row1 = new InlineKeyboardRow(button1, button2, button3);

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().keyboardRow(row1).build();

        message.setText("Выберите операционную систему устройства:");
        message.setReplyMarkup(markup);

        return message;
    }

    public static void log(String first_name, String last_name, String user_id, String txt, String bot_answer) {
        System.out.println("\n ----------------------------");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        System.out.println("Message from " + first_name + " " + last_name + ". (id = " + user_id + ") \n Text - " + txt);
        System.out.println("Bot answer: \n Text - " + bot_answer);
    }
}
