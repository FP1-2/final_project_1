//package com.facebook.controller;
//
//import com.facebook.service.AppUserService;
//import com.facebook.service.FriendshipService;
//import com.facebook.utils.EX;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@Log4j2
//@RestController
//@RequestMapping("friends")
//@RequiredArgsConstructor
//public class FriendshipController {
//
//    private final AppUserService userService;
//
//    private final FriendshipService friendshipService;
//
//    @PostMapping("/add")
//    public ResponseEntity<?> addFriendship(@RequestParam Long userId, @RequestParam Long friendId) {
////        // Реалізуйте логіку для додавання дружби
////        // Перевірте, чи існують користувачі з вказаними ідентифікаторами та додайте відношення дружби в базу даних
////        return ResponseEntity.ok("Friendship added successfully");
//        throw EX.NI;
//    }
//
//    @PostMapping("/remove")
//    public ResponseEntity<?> removeFriendship(@RequestParam Long userId, @RequestParam Long friendId) {
////        // Реалізуйте логіку для видалення дружби
////        // Перевірте, чи існує відношення дружби між користувачами та видаліть його з бази даних
////        return ResponseEntity.ok("Friendship removed successfully");
//        throw EX.NI;
//    }
//}
