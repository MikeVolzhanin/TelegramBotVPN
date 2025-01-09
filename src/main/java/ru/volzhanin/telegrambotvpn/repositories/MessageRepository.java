package ru.volzhanin.telegrambotvpn.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.volzhanin.telegrambotvpn.entity.Message;

import java.util.UUID;

public interface MessageRepository extends MongoRepository<UUID, Message> {
}
