package ru.volzhanin.telegrambotvpn.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.volzhanin.telegrambotvpn.entity.Account;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.UUID;

import static ru.volzhanin.telegrambotvpn.util.Answer.*;

@Service
@RequiredArgsConstructor
public class TelegramService {
    private final AccountService acc;
    private final MessageService msg;

    public SendMessage start(Long userId, Long chatId, String username){
        SendMessage sendMessage;

        if(acc.isAccExist(userId)){
            sendMessage = SendMessage.builder().chatId(chatId).text("""
                    С возвращением, %s! 👋
                                           
                    Рад снова видеть вас! 😊
                                           
                    Как всегда, я готов помочь вам получить конфиг для AmneziaWG на нашем сервере в Нидерландах. 🔒⚡
                                           
                    Ваши преимущества остаются неизменными:
                    🌍 Свобода доступа к любым ресурсам.
                    🔒 Полная конфиденциальность данных.
                    ⚡ Высокая скорость соединения.
                                           
                    Если ваша подписка закончилась, просто следуйте шагам:
                     1️⃣ Изучите инструкцию
                    Перейдите в раздел Инструкция и выберите необходимую операционную систему.
                                                                                        
                     2️⃣ Оплатите подписку
                    Перейдите в раздел Оплата подписки, переведите денежные средства, затем нажмите "Да" и ожидайте, пока бот вышлет вам конфиг.
                                                                                        
                     3️⃣ Получите конфиг
                    Получите готовый конфиг и используйте его с AmneziaWG! 
                    """.formatted(username)).build();
        }
        else{
            acc.addAcc(new Account(UUID.randomUUID(), userId, false, null));
            sendMessage = menuReply(SendMessage.builder().chatId(chatId).text(
                                            """
                                            Приветствую, %s! 👋
                                                                                                
                                            Добро пожаловать в мир VPN! 🤖
                                                                                                
                                            Этот бот поможет вам быстро и удобно создать персонализированный конфиг для AmneziaWG — простого и надежного способа защитить ваши данные в сети. 🚀
                                                                                                
                                            Ваш трафик будет перенаправлен через сервер, расположенный в Нидерландах, что обеспечит:
                                                                                                
                                            🔒 Конфиденциальность: Ваши данные останутся под защитой.
                                            🌍 Свободу доступа: Обходите географические ограничения.
                                             ⚡ Скорость: Высокая производительность серверов.
                                                                                
                                            Чтобы начать:
                                            1️⃣ Изучите инструкцию
                                            Перейдите в раздел Инструкция и выберите необходимую операционную систему.
                                                                                        
                                            2️⃣ Оплатите подписку
                                            Перейдите в раздел Оплата подписки, переведите денежные средства, затем нажмите "Да" и ожидайте, пока бот вышлет вам конфиг.
                                                                                        
                                            3️⃣ Получите конфиг
                                            Получите готовый конфиг и используйте его с AmneziaWG!                     
                                            """.formatted(username)
            ).build());
        }

        return sendMessage;
    }

    public SendMessage status(Long userId, Long chatId){
        SendMessage sendMessage;


        if(acc.isSubscribtionActive(userId)){
            Date currentDate = (Date) acc.getExpirationDate(userId);

            // Текущая дата
            Date now = new Date();

            // Проверка, просрочена ли дата
            if (currentDate.before(now)) {
                sendMessage = SendMessage.builder().chatId(chatId).text(
                        "❌ У вас отсутствует подписка!"
                ).build();
                acc.changeStatusFalse(userId);
                return sendMessage;
            }

            // Создаем объект SimpleDateFormat с нужным форматом
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy");

            // Форматируем дату
            String formattedDate = formatter.format(currentDate);

            sendMessage = SendMessage.builder().chatId(chatId).text(
                    "\uD83D\uDCC5 Дата истечения подписки: %s".formatted(formattedDate)
            ).build();
        }
        else{
            sendMessage = SendMessage.builder().chatId(chatId).text(
                    "❌ У вас отсутствует подписка!"
            ).build();
        }

        return sendMessage;
    }

    public SendMessage paymentQuestion(Long userId, Long chatId){
        SendMessage sendMessage;

        if(acc.isSubscribtionActive(userId)){
            sendMessage = menuReply(SendMessage.builder().chatId(chatId).text("\uD83C\uDF89 У вас уже всё оплачено! \uD83C\uDF89\n" +
                    "✨ Пользуйтесь с удовольствием! ✨").build());
        }
        else{
            sendMessage = questionPaymentAnswer(SendMessage.builder().chatId(chatId).text("""
                    ⚠️ Внимательно прочитайте инструкцию! ⚠️
                    После оплаты бот отправит файл с конфигурацией.
                                        
                    💰 Сумма: 130 руб.
                    🔑 Реквизиты: 2200 7004 6350 8951 (Волжанин М.Р) Т-Банк
                                        
                    📝 Важно! В комментарии к переводу укажите свой никнейм в Telegram. Это необходимо для подтверждения оплаты.
                    
                    Техподдержка: mrvolzhanin@yandex.ru
                                        
                    ✅ Оплатили?
                    Если оплата произведена, нажмите "Да" для получения файла.
                    """).build());
        }

        return sendMessage;
    }

    public SendMessage positivePaymentStatus(Long userId, Long chatId, String username){
        return approvePayment(SendMessage.builder().text("Новая заявка. Оплатил ли пользователь " + username
        + "(" + userId + ")").chatId(Long.valueOf(System.getenv("ADMIN_ID"))).build(), chatId, userId);
    }

    public void changeSubscribtionStatus(Long userId){
        acc.changeStatus(userId);
        System.out.println("---Пользователь " + userId + " подключил подписку.---");
    }
}
