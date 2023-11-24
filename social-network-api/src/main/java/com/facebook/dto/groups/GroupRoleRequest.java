package com.facebook.dto.groups;

import com.facebook.model.groups.GroupRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

/**
 * Запит для фільтрації членів групи за ролями.
 * Використовується в API для визначення ролей членів групи, які потрібно отримати.
 * Містить набір ролей, які можуть бути використані для фільтрації членів групи.
 */
@Data
public class GroupRoleRequest {

    /**
     * Набір ролей для фільтрації.
     * Кожна роль в цьому наборі має відповідати одній з допустимих ролей членів групи.
     * Не може бути null.
     */
    @NotNull(message = "Roles cannot be null")
    private Set<GroupRole> roles;

}
