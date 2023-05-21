package com.ngtu.api.service.impl;

import com.ngtu.api.dto.TokenRequestDTO;
import com.ngtu.api.dto.TokenResponseDTO;
import com.ngtu.api.dto.TokenResult;
import com.ngtu.api.entities.TokenHistory;
import com.ngtu.api.repositories.mongo.TokenHistoryRepository;
import com.ngtu.api.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static com.ngtu.api.common.Constants.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TokenHistoryRepository tokenHistoryRepository;

    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.keycloak.token-uri}")
    private String url;


    @Override
    public TokenResponseDTO getToken(TokenRequestDTO tokenRequestDTO) {
        // Initialize result
        TokenResponseDTO result = new TokenResponseDTO();

        try {
            // Header
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Body
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add(CLIENT_ID, clientId);
            body.add(CLIENT_SECRET, clientSecret);
            body.add(GRANT_TYPE, "password");
            body.add(USERNAME, tokenRequestDTO.getName());
            body.add(PASSWORD, tokenRequestDTO.getCode());

            // Request
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);

            // Call
            ResponseEntity<TokenResult> response = restTemplate.postForEntity(url,request, TokenResult.class);
            if(ObjectUtils.isNotEmpty(response) && ObjectUtils.isNotEmpty(response.getBody()) && StringUtils.isNotBlank(response.getBody().getAccessToken())){
                result.setAccessToken(response.getBody().getAccessToken());
                result.setRefreshToken(response.getBody().getRefreshToken());
                result.setExpiresIn(response.getBody().getExpiresIn());

                // Save History
                TokenHistory tokenHistory = TokenHistory.builder()
                        .grantTpe("password")
                        .accessToken(result.getAccessToken())
                        .refreshToken(result.getRefreshToken())
                        .expiresIn(result.getExpiresIn())
                        .createdBy(tokenRequestDTO.getName())
                        .createdDate(LocalDateTime.now())
                        .ip(tokenRequestDTO.getIp())
                        .build();

                tokenHistoryRepository.save(tokenHistory);
            }

        }catch (Exception e){
            log.error("Error getToken - {}", e.getMessage());
        }

        // Return
        return result;
    }

    @Override
    public TokenResponseDTO refreshToken(TokenRequestDTO tokenRequestDTO) {
        // Initialize result
        TokenResponseDTO result = new TokenResponseDTO();

        try {
            // Header
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Body
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add(CLIENT_ID, clientId);
            body.add(CLIENT_SECRET, clientSecret);
            body.add(GRANT_TYPE, "refresh_token");
            body.add(REFRESH_TOKEN, tokenRequestDTO.getRefreshToken());

            // Request
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);

            // Call
            ResponseEntity<TokenResult> response = restTemplate.postForEntity(url,request, TokenResult.class);
            if(ObjectUtils.isNotEmpty(response) && ObjectUtils.isNotEmpty(response.getBody()) && StringUtils.isNotBlank(response.getBody().getAccessToken())){
                result.setAccessToken(response.getBody().getAccessToken());
                result.setRefreshToken(response.getBody().getRefreshToken());
                result.setExpiresIn(response.getBody().getExpiresIn());

                // Save History
                TokenHistory tokenHistory = TokenHistory.builder()
                        .grantTpe("refresh_token")
                        .accessToken(result.getAccessToken())
                        .refreshToken(result.getRefreshToken())
                        .expiresIn(result.getExpiresIn())
                        .createdDate(LocalDateTime.now())
                        .ip(tokenRequestDTO.getIp())
                        .build();

                tokenHistoryRepository.save(tokenHistory);
            }

        }catch (Exception e){
            log.error("Error refreshToken - {}", e.getMessage());
        }

        // Return
        return result;
    }
}
