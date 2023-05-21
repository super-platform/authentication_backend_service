package com.ngtu.api.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document("token_history")
public class TokenHistory {
    @Id
    private String id;
    private String grantTpe;
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
    private String ip;
    private String createdBy;
    private LocalDateTime createdDate;
}
