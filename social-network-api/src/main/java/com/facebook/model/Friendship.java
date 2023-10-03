//package com.facebook.model;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.NoArgsConstructor;
//
//@Data
//@Entity
//@NoArgsConstructor
//@Table(name = "friendship")
//@EqualsAndHashCode(callSuper = true)
//public class Friendship extends AbstractEntity {
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private Long userId;
//
//    @ManyToOne
//    @JoinColumn(name = "friend_id")
//    private Long friendId;
//
//    @Enumerated(EnumType.STRING)
//    private FriendshipStatus status;
//}
