package ru.volzhanin.telegrambotvpn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Document
@Data
@AllArgsConstructor
public class Account {
    @Id
    UUID Id;
    Long userId;
    Boolean status;
    Date datePayment;
}
