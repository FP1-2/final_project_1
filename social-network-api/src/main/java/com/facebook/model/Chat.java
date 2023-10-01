package com.facebook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name ="chat")
@EqualsAndHashCode(callSuper = true)
public class Chat extends AbstractEntity {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "chat_user",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<AppUser> chatParticipants = new ArrayList<>();

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    List<Message> messages = new ArrayList<>();

    public List<AppUser> addChatParticipants(AppUser user1, AppUser user2){
        chatParticipants.add(user1);
        chatParticipants.add(user2);
        return chatParticipants;
    }
    public List<Message>  addMess(AppUser u, Chat ch, MessageStatus ms){
        Message message = new Message("Hello", u,ch, ms);
        messages.add(message);
        return messages;
    }
}
