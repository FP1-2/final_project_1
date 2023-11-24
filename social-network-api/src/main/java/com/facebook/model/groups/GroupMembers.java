package com.facebook.model.groups;

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

/**
 * Клас, що представляє членство користувачів у групі.
 * Кожен екземпляр цього класу відображає зв'язок між користувачем і групою,
 * а також ролі користувача в цій групі.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "group_members")
@EqualsAndHashCode(callSuper = true)
public class GroupMembers extends AbstractEntity {

    /**
     * Роздільник для зберігання переліку ролей у рядковому форматі.
     */
    private static final String DELIMITER = ":";

    /**
     * Користувач, який є членом групи.
     * Визначається відношенням "багато до одного".
     */
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private AppUser user;

    /**
     * Група, до якої належить користувач.
     * Визначається відношенням "багато до одного".
     */
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private Group group;

    /**
     * Рядок, що містить ролі користувача у групі.
     * Ролі розділяються за допомогою {@link GroupMembers#DELIMITER}.
     */
    @Column(nullable = false)
    private String roles;

    /**
     * Встановлює ролі користувача в групі.
     *
     * @param roles Масив ролей {@link GroupRole}, які будуть присвоєні користувачу.
     */
    public void setRoles(GroupRole[] roles) {
        this.roles = Arrays.stream(roles)
                .map(GroupRole::name)
                .collect(Collectors.joining(DELIMITER));
    }

    /**
     * Отримує ролі користувача в групі.
     *
     * @return Масив ролей {@link GroupRole}, які належать користувачу.
     */
    public GroupRole[] getRoles() {
        return Arrays.stream(this.roles.split(DELIMITER))
                .map(GroupRole::valueOf)
                .toArray(GroupRole[]::new);
    }

}
