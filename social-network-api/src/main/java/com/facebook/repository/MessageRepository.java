package com.facebook.repository;

import com.facebook.model.AppUser;
import com.facebook.model.chat.Message;
import com.facebook.model.chat.MessageStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findAllByChatIdOrderByCreatedDateDesc(Long chatId, Pageable pageable);
    List<Message> findAllByStatusEqualsAndChat_ChatParticipantsContaining(MessageStatus status, AppUser user);

}
