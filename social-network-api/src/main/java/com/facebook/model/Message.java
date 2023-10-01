package com.facebook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "message")
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Message extends AbstractEntity {
    @Column(nullable = false)
    private String text;
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private AppUser sender;
//    @ManyToOne
//    @JoinColumn(name = "receiver_id", nullable = false)
//    private AppUser receiver;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    private MessageStatus status;
}
