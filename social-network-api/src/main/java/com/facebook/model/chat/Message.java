package com.facebook.model.chat;

import com.facebook.model.AbstractEntity;
import com.facebook.model.AppUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "message")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Message extends AbstractEntity {
    @Column(nullable = false)
    private ContentType contentType;
    private String content;
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private AppUser sender;
    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;
    private MessageStatus status;

    public static Message of(ContentType contentType, String content, AppUser sender, Chat chat, MessageStatus status){
        Message newMessage = new Message();
        newMessage.setContentType(contentType);
        newMessage.setContent(content);
        newMessage.setSender(sender);
        newMessage.setChat(chat);
        newMessage.setStatus(status);

        return newMessage;
    }
}
