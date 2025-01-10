package ru.volzhanin.telegrambotvpn.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.volzhanin.telegrambotvpn.entity.Message;

import java.util.UUID;

@Repository
public interface MessageRepository extends MongoRepository<Message, UUID> {
}
