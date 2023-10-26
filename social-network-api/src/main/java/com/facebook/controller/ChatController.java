package com.facebook.controller;

import com.facebook.dto.chat.ChatResponse;
import com.facebook.dto.chat.ChatResponseList;
import com.facebook.dto.message.MessageResponse;
import com.facebook.facade.ChatFacade;
import com.facebook.facade.MessageFacade;
import com.facebook.service.ChatService;
import com.facebook.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("api/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
//    private final MessageService messageService;
    private final MessageFacade messageFacade;
    private final ChatFacade chatFacade;
    @GetMapping
    public ResponseEntity<List<ChatResponseList>> getAllChats(
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        List<ChatResponseList> allUserChats = chatFacade.getAllUserChats(pageable);
        return ResponseEntity.ok(allUserChats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatResponse> getChatById(@PathVariable long id){
        ChatResponse chatResponse = chatFacade.getChatById(id);
        log.info("Get Chat with ID: "+ id);
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        log.info("Princ name" + principal.getName());
        return ResponseEntity.ok(chatResponse);
    }

    @PostMapping("/{username}")
    public ResponseEntity<ChatResponse> createChat(@PathVariable String username){
        ChatResponse chatRes = chatFacade.createChat(username);
        log.info("New chat created. Chat: "+chatRes);
        return ResponseEntity.ok(chatRes);
    }
    @GetMapping("/messages/{chatId}")
    public ResponseEntity<List<MessageResponse>> getAllMessages(@PathVariable long chatId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        List<MessageResponse> allMessages = messageFacade.getAllMessages(chatId, pageable);
        return ResponseEntity.ok(allMessages);
    }

    @GetMapping("/messages/unread")
    public ResponseEntity<Long> getCountOfUnreadMessage(){
        return ResponseEntity.ok(messageFacade.countUnreadMessage());
    }
    @GetMapping("/search")
    public ResponseEntity<List<ChatResponse>> searchChats(@RequestParam String input,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "20") int size){
        Pageable pageable = PageRequest.of(page, size);
        List<ChatResponse> allChats = chatFacade.searchChats(input, pageable);
        return ResponseEntity.ok(allChats);
    }
}
