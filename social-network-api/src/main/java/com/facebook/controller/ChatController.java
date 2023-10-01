package com.facebook.controller;

import com.facebook.dto.appuser.AppUserResponse;
import com.facebook.dto.chat.ChatResponse;
import com.facebook.dto.chat.ChatResponseList;
import com.facebook.model.Chat;
import com.facebook.service.AppUserService;
import com.facebook.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("api/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<List<ChatResponseList>> getAllChats(){
        List<ChatResponseList> allUserChats = chatService.getAllUserChats();
        return ResponseEntity.ok(allUserChats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatResponse> getChatById(@PathVariable long id){
        ChatResponse chatResponse = chatService.chatById(id);
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
