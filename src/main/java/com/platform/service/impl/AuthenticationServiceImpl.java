package com.platform.service.impl;

import com.platform.dto.LoginRequestDTO;
import com.platform.dto.RefreshTokenRequestDTO;
import com.platform.dto.TokenResponseDTO;
import com.platform.dto.TokenResult;
import com.platform.service.AuthenticationService;
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

import static com.platform.common.Constants.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

//    private final TokenHistoryRepository tokenHistoryRepository;

    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.keycloak.token-uri}")
    private String url;


    @Override
    public TokenResponseDTO getToken(LoginRequestDTO loginRequestDTO) {
        // Initialize result
        TokenResponseDTO result = new TokenResponseDTO();

        // Header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(CLIENT_ID, clientId);
        body.add(CLIENT_SECRET, clientSecret);
        body.add(GRANT_TYPE, "password");
        body.add(USERNAME, loginRequestDTO.getUsername());
        body.add(PASSWORD, loginRequestDTO.getPassword());

        // Request
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);

        // Call
        ResponseEntity<TokenResult> response = restTemplate.postForEntity(url,request, TokenResult.class);
        if(ObjectUtils.isNotEmpty(response) && ObjectUtils.isNotEmpty(response.getBody()) && StringUtils.isNotBlank(response.getBody().getAccessToken())){
            result.setAccessToken(response.getBody().getAccessToken());
            result.setRefreshToken(response.getBody().getRefreshToken());
            result.setExpiresIn(response.getBody().getExpiresIn());

            // Save History
//                TokenHistory tokenHistory = TokenHistory.builder()
//                        .grantTpe("password")
//                        .accessToken(result.getAccessToken())
//                        .refreshToken(result.getRefreshToken())
//                        .expiresIn(result.getExpiresIn())
//                        .createdBy(tokenRequestDTO.getName())
//                        .createdDate(LocalDateTime.now())
//                        .ip(tokenRequestDTO.getIp())
//                        .build();
//
//                tokenHistoryRepository.save(tokenHistory);
        }

        // Return
        return result;
    }

    @Override
    public TokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        // Initialize result
        TokenResponseDTO result = new TokenResponseDTO();

        // Header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(CLIENT_ID, clientId);
        body.add(CLIENT_SECRET, clientSecret);
        body.add(GRANT_TYPE, "refresh_token");
        body.add(REFRESH_TOKEN, refreshTokenRequestDTO.getRefreshToken());

        // Request
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);

        // Call
        ResponseEntity<TokenResult> response = restTemplate.postForEntity(url,request, TokenResult.class);
        if(ObjectUtils.isNotEmpty(response) && ObjectUtils.isNotEmpty(response.getBody()) && StringUtils.isNotBlank(response.getBody().getAccessToken())){
            result.setAccessToken(response.getBody().getAccessToken());
            result.setRefreshToken(response.getBody().getRefreshToken());
            result.setExpiresIn(response.getBody().getExpiresIn());

            // Save History
//                TokenHistory tokenHistory = TokenHistory.builder()
//                        .grantTpe("refresh_token")
//                        .accessToken(result.getAccessToken())
//                        .refreshToken(result.getRefreshToken())
//                        .expiresIn(result.getExpiresIn())
//                        .createdDate(LocalDateTime.now())
//                        .ip(tokenRequestDTO.getIp())
//                        .build();
//
//                tokenHistoryRepository.save(tokenHistory);
        }

        // Return
        return result;
    }
}
