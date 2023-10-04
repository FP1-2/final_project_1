package com.facebook.controller;

import com.facebook.dto.chat.ChatResponse;
import com.facebook.dto.chat.ChatResponseList;
import com.facebook.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("api/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<List<ChatResponseList>> getAllChats(
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        List<ChatResponseList> allUserChats = chatService.getAllUserChats(pageable);
        return ResponseEntity.ok(allUserChats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatResponse> getChatById(@PathVariable long id){
        ChatResponse chatResponse = chatService.getChatById(id);
        log.info("Get Chat with ID: "+ id);
        return ResponseEntity.ok(chatResponse);
    }

    @PostMapping("/{username}")
    public ResponseEntity<ChatResponse> createChat(@PathVariable String username){
        ChatResponse chatRes = chatService.createNewChat(username);
        log.info("New chat created. Chat: "+chatRes);
        return ResponseEntity.ok(chatRes);
    }
}
