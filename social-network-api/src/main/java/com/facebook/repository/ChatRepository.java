package com.facebook.repository;

import com.facebook.model.AppUser;
import com.facebook.model.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Page<Chat> findByChatParticipantsContainingOrderByLastModifiedDateDesc(AppUser user, Pageable pageable);
    List<Chat> findChatsByChatParticipantsContainsAndChatParticipantsContains(AppUser user, AppUser user2);
}
