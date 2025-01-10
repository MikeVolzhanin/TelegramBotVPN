package ru.volzhanin.telegrambotvpn.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.volzhanin.telegrambotvpn.service.TelegramService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static ru.volzhanin.telegrambotvpn.util.Answer.*;

@Component
@RequiredArgsConstructor
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    @Autowired
    private TelegramService telegramService;
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
            String user_username = update.getCallbackQuery().getMessage().getChat().getUserName();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            long user_id = update.getCallbackQuery().getMessage().getChat().getId();
            String callbackData = update.getCallbackQuery().getData();

            if ("android_clicked".equals(callbackData)) {
                sendMessage = menuReply(SendMessage.builder().text("""
                        📱 Инструкция по использованию AmneziaWG на Android
                        Шаг 1: Подготовка               
                        Перейдите в Google Play и скачайте приложение AmneziaWG (https://play.google.com/store/apps/details?id=org.amnezia.awg&hl=ru&pli=1).
                                
                        После оформления подписки на вашем аккаунте бот отправит вам конфиг. Это файл с расширением .conf.
                        Сохраните его на своем устройстве
                                                
                        Шаг 2: Импорт конфигурации                                                                     
                        Откройте приложение AmneziaWG.
                        
                        Нажмите "+" для добавления нового туннеля.
                        Выберите "Импортировать файл или архив" и найдите файл конфигурации, полученный от бота.
                                                
                        Шаг 3: Подключение
                        Вернитесь на главный экран приложения. Найдите свой импортированный конфиг и активируйте его.
                        
                        Теперь ваше приложение настроено и готово к работе! 🔐               
                        """).chatId(chat_id).build());
            } else if ("IOS_clicked".equals(callbackData)) {
                sendMessage = menuReply(SendMessage.builder().text("""
                        📱 Инструкция по использованию AmneziaWG на iOS
                        Шаг 1: Подготовка
                        Скачайте и установите приложение
                        Перейдите в App Store и скачайте приложение AmneziaWG (https://apps.apple.com/ru/app/amneziawg/id6478942365).
                        Нажмите "Загрузить" и дождитесь завершения установки.
                                               
                        После оформления подписки на вашем аккаунте бот отправит вам конфиг. Это файл с расширением .conf.
                        Сохраните его на своем устройстве (обычно в разделе «Файлы» или через iCloud).
                                                
                        Шаг 2: Импорт конфигурации
                        Запустите приложение AmneziaWG на вашем iPhone или iPad.
                                                
                        Добавьте новый туннель
                        Нажмите на кнопку "+" (или "Добавить новый туннель").
                                                
                        Импортируйте конфиг
                        Выберите "Создать из файла или архива", найдите файл .conf, который вам прислал бот, и выберите его.
                                                      
                        Шаг 3: Подключение
                        Вернитесь на главный экран приложения. Найдите свой импортированный конфиг и активируйте его.
                                                
                        Подключение завершено
                        Как только конфиг активируется, ваше соединение будет защищено. Вы можете наслаждаться безопасным использованием!
                        
                        Теперь ваше приложение настроено и готово к работе! 🔐
                        """).chatId(chat_id).build());
            } else if ("windows_clicked".equals(callbackData)) {
                sendMessage = menuReply(SendMessage.builder().text("""
                        Шаг 1: Подготовка
                        Перейдите на сайт AmneziaWG и скачайте установочный файл для Windows.
                        Запустите установку и следуйте инструкциям на экране.
                        Сайт: https://amnezia.org/ru/downloads                        
                        
                        После оформления подписки на вашем аккаунте бот отправит вам конфиг. Это файл с расширением .conf.
                        Сохраните его на вашем компьютере.
                        Шаг 2: Импорт конфигурации
                        Запустите приложение AmneziaWG на вашем ПК.
                                               
                        Нажмите кнопку "+" (находится на нижней панели).
                                                
                        Выберите "Файл с настройками подключения", найдите файл .conf, который вам прислал бот, и выберите его для импорта.
                                                
                        Шаг 3: Подключение
                        После успешного импорта вернитесь в главный экран приложения. Найдите свой импортированный конфиг и активируйте его.
                                                
                        Когда конфиг будет активирован, ваше соединение будет защищено, и вы сможете использовать его для безопасного подключения!
                        
                        Теперь ваше приложение настроено и готово к работе! 🔐
                        """).chatId(chat_id).build());
            } else if("yes_payment_clicked".equals(callbackData)){
                try {
                    telegramClient.execute(telegramService.positivePaymentStatus(user_id, chat_id, user_username));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                sendMessage = SendMessage.builder().chatId(chat_id).text("Хорошо! Ожидайте)").build();
            } else if("no_payment_clicked".equals(callbackData.split(" ")[0])){
                sendMessage = menuReply(SendMessage.builder().chatId(chat_id).text("Печально(").build());
            } else if ("yes_payment_received".equals(callbackData.split(" ")[0])) {
                EditMessageReplyMarkup editMessageReplyMarkup = EditMessageReplyMarkup.builder()
                        .chatId(Long.valueOf(System.getenv("ADMIN_ID")))
                        .messageId(update.getCallbackQuery().getMessage().getMessageId())
                        .replyMarkup(null) // Убираем клавиатуру
                        .build();
                try {
                    telegramClient.execute(editMessageReplyMarkup);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                chat_id = Long.parseLong(callbackData.split(" ")[1]);
                user_id = Long.parseLong(callbackData.split(" ")[2]);
                // Путь к папке free
                File freeFolder = new File("files/free");

                // Получаем список файлов из папки free
                File[] files = freeFolder.listFiles();
                if (files == null || files.length == 0) {
                    try {
                        telegramClient.execute(SendMessage.builder().chatId(Long.valueOf(System.getenv("ADMIN_ID"))).text("Нет доступных файлов для отправки.").build());
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Нет доступных файлов для отправки.");
                    return;
                }
                // Берем первый файл
                File fileToSend = files[0];

                // Отправляем файл
                SendDocument sendDocument = menuReply(SendDocument.builder().chatId(chat_id).document(new InputFile(fileToSend)).caption("Ваша конфигурация").build());

                try {
                    telegramClient.execute(sendDocument);
                    System.out.println("Файл отправлен: " + fileToSend.getName());
                    telegramClient.execute(SendMessage.builder().chatId(Long.valueOf(System.getenv("ADMIN_ID"))).text("Файл отправлен пользователю " + user_username).build());
                    // Перемещаем файл в папку used
                    File usedFolder = new File("files/used");
                    Path targetPath = new File(usedFolder, fileToSend.getName()).toPath();
                    Files.move(fileToSend.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Файл перемещен в папку used: " + targetPath);
                } catch (TelegramApiException e) {
                    System.err.println("Ошибка отправки файла: " + e.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                telegramService.changeSubscribtionStatus(user_id);
                return;
            }
            log(user_first_name,user_last_name, Long.toString(chat_id), callbackData, Objects.requireNonNull(sendMessage).getText().substring(0, 15) + "...");
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            String receivedText = update.getMessage().getText();
            String user_first_name = update.getMessage().getChat().getFirstName();
            String user_last_name = update.getMessage().getChat().getLastName();
            String user_username = update.getMessage().getChat().getUserName();

            long user_id = update.getMessage().getChat().getId();
            long chat_id = update.getMessage().getChatId();

            switch (receivedText) {
                case "/start" :
                    sendMessage = menuReply(telegramService.start(user_id, chat_id, (user_first_name == null) ? user_username : user_first_name));
                    break;

                case "Инструкция" :
                    sendMessage = instructionOptions(SendMessage.builder().chatId(chat_id).text("default").build());
                    break;
                case "Статус подписки" :
                    sendMessage = menuReply(telegramService.status(user_id, chat_id));
                    break;

                case "Оплата подписки" :
                    sendMessage = telegramService.paymentQuestion(user_id, chat_id);
                    break;
            }

            assert sendMessage != null;
            log(user_first_name, user_last_name, Long.toString(chat_id), receivedText, sendMessage.getText().substring(0, 20) + "...");
        }

        try {
            if (sendMessage == null){
                System.out.println("---Бот получил невалидное сообщение или был заблокирован.---");
                return;
            }
            telegramClient.execute(sendMessage); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
