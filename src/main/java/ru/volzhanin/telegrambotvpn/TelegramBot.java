package ru.volzhanin.telegrambotvpn;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    public TelegramBot(){
        telegramClient = new OkHttpTelegramClient(getBotToken());
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String receivedText = update.getMessage().getText();
            String user_first_name = update.getMessage().getChat().getFirstName();
            String user_last_name = update.getMessage().getChat().getLastName();
            String user_username = update.getMessage().getChat().getUserName();
            long user_id = update.getMessage().getChat().getId();
            long chat_id = update.getMessage().getChatId();

            SendMessage sendMessage = null;

            switch (receivedText) {
                case "/start" -> sendMessage = createReplyKeyboard(SendMessage.builder().chatId(chat_id).text(
                        """
                            Приветствую, %s!
                            Наш VPN самый пиздатый и у нас самые пиздатые условия.
                            Если не с нами, то ты пидарас))
                        """.formatted(user_first_name)
                ).build());

                case "Инструкция" ->
                        sendMessage = instructionOptions(SendMessage.builder().chatId(chat_id).text("Скачайте AmneziaWG").build());

                case "Статус подписки" ->
                        sendMessage = createReplyKeyboard(SendMessage.builder().chatId(chat_id).text("Ваша подписка отсутсвует!").build());

                case "Оплата подписки" ->
                        sendMessage = createReplyKeyboard(SendMessage.builder().chatId(chat_id).text("""
                        Сумма: 130 руб.
                        Реквизиты: 2200 0000 3333 000 (Иванов И.И).
                        В течение 30 минут ожидайте необходимый файл с настройками!
                        """).build());
            }

            log(user_first_name, user_last_name, Long.toString(user_id), receivedText, sendMessage.getText());
            try {
                telegramClient.execute(sendMessage); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public SendMessage createReplyKeyboard(SendMessage message) {
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

    private SendMessage instructionOptions(SendMessage message) {
        InlineKeyboardButton button1 = InlineKeyboardButton.builder().text("Android").callbackData("android_clicked").build();
        InlineKeyboardButton button2 = InlineKeyboardButton.builder().text("IOS").callbackData("IOS_clicked").build();
        InlineKeyboardButton button3 = InlineKeyboardButton.builder().text("Windows").callbackData("windows_clicked").build();

        InlineKeyboardRow row1 = new InlineKeyboardRow(button1, button2, button3);

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder().keyboardRow(row1).build();

        message.setText("Выберите операционную систему устройства:");
        message.setReplyMarkup(markup);

        return message;
    }

    private void log(String first_name, String last_name, String user_id, String txt, String bot_answer) {
        System.out.println("\n ----------------------------");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        System.out.println("Message from " + first_name + " " + last_name + ". (id = " + user_id + ") \n Text - " + txt);
        System.out.println("Bot answer: \n Text - " + bot_answer);
    }

}
