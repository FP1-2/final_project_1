package com.facebook.repository;

import com.facebook.model.AppUser;
import com.facebook.model.chat.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Page<Chat> findByChatParticipantsContainingOrderByLastModifiedDateDesc(AppUser user, Pageable pageable);
    List<Chat> findChatsByChatParticipantsContainsAndChatParticipantsContains(AppUser user, AppUser user2);

    @Query(value =
                    "SELECT DISTINCT c.id, c.created_date, c.last_modified_date, " +
                        "CASE WHEN u1.id = :authUserId THEN u2.id ELSE u1.id END as user_id, " +
                        "CASE WHEN u1.id = :authUserId THEN u2.name ELSE u1.name END as name, " +
                        "CASE WHEN u1.id = :authUserId THEN u2.surname ELSE u1.surname END as surname, " +
                        "CASE WHEN u1.id = :authUserId THEN u2.username ELSE u1.username END as username, " +
                        "CASE WHEN u1.id = :authUserId THEN u2.avatar ELSE u1.avatar END as avatar " +
                    "FROM chat c " +
                    "JOIN chat_user cu1 ON c.id = cu1.chat_id " +
                    "JOIN chat_user cu2 ON c.id = cu2.chat_id " +
                    "JOIN users u1 ON cu1.user_id = u1.id " +
                    "JOIN users u2 ON cu2.user_id = u2.id " +
                    "WHERE ( (u1.id = :authUserId AND u2.id <> :authUserId) " +
                    "AND CONCAT(LOWER(u2.name), ' ', LOWER(u2.surname)) LIKE LOWER(CONCAT('%', :input, '%'))) " +
                    "OR ( (u2.id = :authUserId AND u1.id <> :authUserId)" +
                    "AND CONCAT(LOWER(u1.name), ' ', LOWER(u1.surname)) LIKE LOWER(CONCAT('%', :input, '%')))" +
                    "GROUP BY c.id, u1.id, u2.id",
            nativeQuery = true)

    List<Map<String, Object>> findChatsByKeyword(@Param("authUserId") Long id, @Param("input") String input, Pageable pageable);

}
