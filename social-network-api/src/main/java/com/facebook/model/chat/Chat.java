package com.facebook.model.chat;

import com.facebook.model.AbstractEntity;
import com.facebook.model.AppUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public static Chat of(Long id, LocalDateTime createdAt, LocalDateTime lastModifiedDate){
        Chat chat = new Chat();
        chat.setId(id);
        chat.setCreatedDate(createdAt);
        chat.setLastModifiedDate(lastModifiedDate);
        return chat;
    }
}
