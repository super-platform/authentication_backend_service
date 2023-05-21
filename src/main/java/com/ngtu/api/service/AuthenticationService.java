package com.ngtu.api.service;

import com.ngtu.api.dto.TokenRequestDTO;
import com.ngtu.api.dto.TokenResponseDTO;

public interface AuthenticationService {
    TokenResponseDTO getToken(TokenRequestDTO tokenRequestDTO);
    TokenResponseDTO refreshToken(TokenRequestDTO tokenRequestDTO);
}
