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

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Message> messages = new ArrayList<>();
    public static Chat of(AppUser user1, AppUser user2){
        Chat chat = new Chat();
        chat.setChatParticipants(List.of(user1, user2));
        return chat;
    }
}
