package com.facebook.model.chat;

import com.facebook.model.AbstractEntity;
import com.facebook.model.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "message")
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
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
}
