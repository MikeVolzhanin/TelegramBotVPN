package ru.volzhanin.telegrambotvpn.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.volzhanin.telegrambotvpn.entity.Account;

import java.util.UUID;

@Repository
public interface AccountRepository extends MongoRepository<Account, UUID> {
    Account findByUserId(Long userId);
}
