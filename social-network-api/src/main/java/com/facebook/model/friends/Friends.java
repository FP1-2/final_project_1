//package com.facebook.model.friends;
//
//import com.facebook.model.AbstractEntity;
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//
//@EqualsAndHashCode(callSuper = true)
//@Data
////@Entity
////@NoArgsConstructor
//public class Friends extends AbstractEntity {
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private Long userId;
//
//    @ManyToOne
//    @JoinColumn(name = "friend_id")
//    private Long friendId;
//
//    @Enumerated(EnumType.STRING)
//    private FriendsStatus status;
//}
