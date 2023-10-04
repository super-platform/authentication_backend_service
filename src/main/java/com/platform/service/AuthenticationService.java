package com.platform.service;

import com.platform.dto.LoginRequestDTO;
import com.platform.dto.RefreshTokenRequestDTO;
import com.platform.dto.TokenResponseDTO;

public interface AuthenticationService {
    TokenResponseDTO getToken(LoginRequestDTO loginRequestDTO);
    TokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO);
}
