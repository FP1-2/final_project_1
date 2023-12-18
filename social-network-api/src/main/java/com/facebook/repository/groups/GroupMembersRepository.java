package com.facebook.repository.groups;

import com.facebook.model.groups.GroupMembers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GroupMembersRepository extends JpaRepository<GroupMembers, Long> {

    /**
     * Знаходить члена групи за ідентифікатором користувача та ідентифікатором групи.
     *
     * @param userId  Ідентифікатор користувача.
     * @param groupId Ідентифікатор групи.
     * @return Optional об'єкт, що містить члена групи або порожній, якщо член не знайдений.
     */
    Optional<GroupMembers> findByUserIdAndGroupId(Long userId, Long groupId);

    /**
     * Знаходить членів групи за заданим ідентифікатором групи та ролями.
     * Дозволяє фільтрувати членів групи на основі їх ролей (ADMIN, MEMBER, BANNED)
     * з використанням гнучких параметрів запиту.
     *
     * @param groupId Ідентифікатор групи, члени якої повинні бути знайдені.
     * @param roleMember Рядкове представлення ролі MEMBER, якщо вона повинна бути включена у фільтрацію.
     * @param roleAdmin Рядкове представлення ролі ADMIN, якщо вона повинна бути включена у фільтрацію.
     * @param roleBanned Рядкове представлення ролі BANNED, якщо вона повинна бути включена у фільтрацію.
     * @param pageable Параметри пагінації та сортування для оптимізації видачі результатів.
     * @return Сторінка (Page) членів групи, які відповідають заданим критеріям фільтрації.
     */
    @Query("""
       SELECT gm
       FROM GroupMembers gm
       WHERE gm.group.id = :groupId
         AND ( (:roleMember = '' AND :roleAdmin = '' AND :roleBanned = '')
             OR (:roleMember != '' AND gm.roles LIKE %:roleMember%)
             OR (:roleAdmin != '' AND gm.roles LIKE %:roleAdmin%)
             OR (:roleBanned != '' AND gm.roles LIKE %:roleBanned%)
         )
       """)
    Page<GroupMembers> findMembersByGroupIdAndRoles(@Param("groupId") Long groupId,
                                                    @Param("roleMember") String roleMember,
                                                    @Param("roleAdmin") String roleAdmin,
                                                    @Param("roleBanned") String roleBanned,
                                                    Pageable pageable);

    /**
     * Знаходить всіх членів групи за ідентифікатором групи та ідентифікаторами користувачів.
     *
     * @param groupId Ідентифікатор групи.
     * @param userIds Набір ідентифікаторів користувачів.
     * @return Список GroupMembers.
     */
    @Query("""
            SELECT gm FROM GroupMembers gm
            WHERE gm.group.id = :groupId AND gm.user.id IN :userIds
            """)
    List<GroupMembers> findAllByGroupIdAndUserIds(@Param("groupId") Long groupId,
                                                  @Param("userIds") Set<Long> userIds);

    boolean existsByUserIdAndGroupId(Long userId, Long groupId);

    /** For gen */
    @Query("SELECT gm.user.id FROM GroupMembers gm WHERE gm.group.id = :groupId")
    List<Long> findMemberIdsByGroupId(@Param("groupId") Long groupId);
}

