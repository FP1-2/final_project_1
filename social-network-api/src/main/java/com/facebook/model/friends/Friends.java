package com.facebook.model.friends;

import com.facebook.model.AbstractEntity;
import com.facebook.model.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "friends",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "friend_id"}))
public class Friends extends AbstractEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private AppUser user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "friend_id", referencedColumnName = "id", nullable = false)
    private AppUser friend;

    @Enumerated(EnumType.STRING)
    private FriendsStatus status;
}
