package com.facebook.model.group;

import com.facebook.model.AbstractEntity;
import com.facebook.model.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;

@Data
@Entity
@NoArgsConstructor
@Table(name = "group_memberships")
@EqualsAndHashCode(callSuper = true)
public class GroupMembership extends AbstractEntity {

    private static final String DELIMITER = ":";

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private Group group;


    @Column(nullable = false)
    private String roles;

    public void setRoles(GroupRole[] roles) {
        this.roles = Arrays.stream(roles)
                .map(GroupRole::name)
                .collect(Collectors.joining(DELIMITER));
    }

    public GroupRole[] getRoles() {
        return Arrays.stream(this.roles.split(DELIMITER))
                .map(GroupRole::valueOf)
                .toArray(GroupRole[]::new);
    }

}
