package ru.volzhanin.telegrambotvpn.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@Data
public class Message {
    @Id
    UUID Id;
    String name;
    String text;
}
