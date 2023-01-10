package ru.skillbox.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.enums.NameNotification;
//todo удалить если не используется

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification")
public class Notification {

    /**
     * id оповещения
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "person_id")
    private Long personId;

    @Column(name = "author_id")
    private Long authorId;

    private String content;

    @Column(name = "name_notification")
    @Enumerated(EnumType.STRING)
    private NameNotification nameNotification;

    private boolean read;



}