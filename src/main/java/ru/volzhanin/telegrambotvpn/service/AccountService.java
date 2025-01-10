package ru.volzhanin.telegrambotvpn.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.volzhanin.telegrambotvpn.entity.Account;
import ru.volzhanin.telegrambotvpn.repositories.AccountRepository;

import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accRepo;

    public Boolean isAccExist(Long userId){
        return accRepo.findByUserId(userId) != null;
    }

    public void addAcc(Account account){
        accRepo.save(account);
    }

    public Boolean isSubscribtionActive(Long userId){
        Account account = accRepo.findByUserId(userId);
        return account.getStatus();
    }

    public Date getExpirationDate(Long userId){
        Account account = accRepo.findByUserId(userId);
        return account.getDatePayment();
    }

    public void changeStatus(Long userId){
        Date current = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.add(Calendar.MONTH, 1);

        Account account = accRepo.findByUserId(userId);
        account.setStatus(true);
        account.setDatePayment(calendar.getTime());

        accRepo.deleteById(account.getId());
        accRepo.save(account);
    }

    public void changeStatusFalse(Long userId){
        Account account = accRepo.findByUserId(userId);
        account.setStatus(false);
        account.setDatePayment(null);

        accRepo.deleteById(account.getId());
        accRepo.save(account);
    }
}
