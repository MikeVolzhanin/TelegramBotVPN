package ru.volzhanin.telegrambotvpn.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.volzhanin.telegrambotvpn.entity.Account;

import java.util.UUID;

public interface AccountRepository extends MongoRepository<UUID, Account> {
}
