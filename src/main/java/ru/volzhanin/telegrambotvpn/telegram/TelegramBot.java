package ru.volzhanin.telegrambotvpn.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import static ru.volzhanin.telegrambotvpn.util.Answer.*;

@Component
@RequiredArgsConstructor
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

        SendMessage sendMessage = null;

        if(update.hasCallbackQuery()){
            String user_first_name = update.getCallbackQuery().getMessage().getChat().getFirstName();
            String user_last_name = update.getCallbackQuery().getMessage().getChat().getLastName();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            String callbackData = update.getCallbackQuery().getData();

            if ("android_clicked".equals(callbackData)) {
                sendMessage = createReplyKeyboard(SendMessage.builder().text("Android").chatId(chat_id).build());
            } else if ("IOS_clicked".equals(callbackData)) {
                sendMessage = createReplyKeyboard(SendMessage.builder().text("IOS").chatId(chat_id).build());
            } else if ("windows_clicked".equals(callbackData)) {
                sendMessage = createReplyKeyboard(SendMessage.builder().text("windows").chatId(chat_id).build());
            }
            log(user_first_name,user_last_name, Long.toString(chat_id), callbackData, sendMessage.getText());
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            String receivedText = update.getMessage().getText();
            String user_first_name = update.getMessage().getChat().getFirstName();
            String user_last_name = update.getMessage().getChat().getLastName();
            String user_username = update.getMessage().getChat().getUserName();

            long user_id = update.getMessage().getChat().getId();
            long chat_id = update.getMessage().getChatId();

            switch (receivedText) {
                case "/start" -> sendMessage = createReplyKeyboard(SendMessage.builder().chatId(chat_id).text(
                        """
                            Приветствую, %s!
                        Наш VPN самый пиздатый и у нас самые пиздатые условия.
                        Если не с нами, то ты пидарас))
                        """.formatted((user_first_name == null) ? user_username : user_first_name)
                ).build());

                case "Инструкция" ->
                        sendMessage = instructionOptions(SendMessage.builder().chatId(chat_id).text("default").build());

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
        }

        try {
            telegramClient.execute(sendMessage); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
