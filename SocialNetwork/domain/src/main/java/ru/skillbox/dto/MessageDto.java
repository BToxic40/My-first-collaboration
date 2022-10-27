package ru.skillbox.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {

    private Long id;
    private Long time;
    private String status;

    @JsonProperty("author_id")
    private Long authorId;

    @JsonProperty("recipient_id")
    private Long recipientId;

    @JsonProperty("message_text")
    private String messageText;
}
