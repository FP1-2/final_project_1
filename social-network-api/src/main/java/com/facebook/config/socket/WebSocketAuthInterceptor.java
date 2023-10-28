package com.facebook.config.socket;

import com.facebook.config.security.AppUserDetails;
import com.facebook.config.security.JwtFilter;
import com.facebook.repository.AppUserRepository;
import com.facebook.service.AppUserService;
import com.facebook.service.JwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Log4j2
@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {
    @Autowired
    JwtTokenService jwtService;
    @Autowired
    private AppUserService appUserService;
    private Message<?> Exception;

    @Override
    public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        final StompCommand cmd = accessor.getCommand();
        if (StompCommand.CONNECT.equals(cmd) || StompCommand.SEND.equals(cmd) || StompCommand.SUBSCRIBE.equals(cmd)) {
            String requestTokenHeader = accessor.getFirstNativeHeader("Authorization");
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer")) {
                String token = requestTokenHeader.substring(7);
                authenticateUser(token).ifPresentOrElse(accessor::setUser, () -> {
                    throw new UsernameNotFoundException("username not found");
                });
            }
        }
        return message;
    }

    private Optional<Principal> authenticateUser(String token){
        Optional<Integer> maybeId = jwtService.parseToken(token);
        maybeId.map(id -> AppUserDetails.of(id,
                        appUserService.findRolesById((long) id),
                        appUserService.findUsernameById((long) id),
                        appUserService.findPasswordById((long) id),
                        jwtService.getExpirationDateFromToken(token)
                ))
                .map(ud -> new UsernamePasswordAuthenticationToken(ud,
                        null, ud.getAuthorities()))
                .ifPresentOrElse(at -> {
                    SecurityContextHolder.getContext().setAuthentication(at);
                }, () -> log.error("Failed to process setup authentication"));
        return Optional.of(SecurityContextHolder.getContext().getAuthentication());
    }

//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
//        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//        Authentication authenticatedUser = null;
//
//        List<String> authorizationHeader = headerAccessor.getNativeHeader("Authorization");
//        if(authorizationHeader != null) {
//            authenticatedUser = authenticateUser(authorizationHeader.get(0));
//            accessor.setUser(authenticatedUser);
//        }
//
//        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
//            if(!validateSubscription(authenticatedUser, headerAccessor.getDestination())) {
//                throw new MessagingException("No tiene permiso para suscribirse a este tópico");
//            }
//        }
//        return message;
//    }
//
//    private boolean validateSubscription(Principal user, String channel) {
//        //User not authenticated
//        if (user == null) {
//            return false;
//        }
//
//        //User trying to subscribe to a channel that doesn't belong to him
//        if(channel.startsWith("/user") && !channel.startsWith("/user/" + user.getName() + "/")) {
//            return false;
//        }
//
//        return true;
//    }

}
