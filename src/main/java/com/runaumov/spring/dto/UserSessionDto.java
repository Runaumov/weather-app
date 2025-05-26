package com.runaumov.spring.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserSessionDto {

    private UUID sessionId;
    private Long userId;
    private String userLogin;
    private LocalDateTime expiresAt;
}
