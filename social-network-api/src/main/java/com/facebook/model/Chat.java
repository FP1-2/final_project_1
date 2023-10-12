package com.facebook.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

//@Data
//@Entity
//@Table(name ="chat")
//@EqualsAndHashCode(callSuper = true)
//public class Chat extends AbstractEntity {
//    @ManyToMany
//    @JoinTable(
//            name = "chat_user",
//            joinColumns = @JoinColumn(name = "chat_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
//    Set<AppUser> chatParticipants;
//
//    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
//    Set<Message> messages;
//}
