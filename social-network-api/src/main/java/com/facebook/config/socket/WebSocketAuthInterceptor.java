package com.facebook.config.socket;

import com.facebook.config.security.AppUserDetails;
import com.facebook.service.AppUserService;
import com.facebook.service.JwtTokenService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
                .ifPresentOrElse(at -> SecurityContextHolder.getContext().setAuthentication(at), () -> log.error("Failed to process setup authentication"));
        return Optional.of(SecurityContextHolder.getContext().getAuthentication());
    }
}
