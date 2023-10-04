package com.platform.controllers.internal;

import com.platform.common.dto.ResponseWithBody;
import com.platform.common.utils.ResponseUtil;
import com.platform.dto.LoginRequestDTO;
import com.platform.dto.RefreshTokenRequestDTO;
import com.platform.dto.TokenResponseDTO;
import com.platform.service.AuthenticationService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.platform.common.constant.CommonConstants.TRACE_ID;


@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication Resources API")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indicates that the request was executed successfully"),
            @ApiResponse(responseCode = "500", description = "Indicates that there is a server error occurs during executing the request")})
    @PostMapping(value = "/token", consumes = "application/json",produces = "application/json")
    public ResponseWithBody<?> getToken(@RequestBody LoginRequestDTO loginRequestDTO){
        String traceId = UUID.randomUUID().toString();
        MDC.put(TRACE_ID, traceId);

        // Initialize result
        ResponseWithBody<TokenResponseDTO> result = new ResponseWithBody<>();

        // Get token
        TokenResponseDTO tokenResponseDTO = authenticationService.getToken(loginRequestDTO);

        result.setBody(tokenResponseDTO);
        result.setStatus(ResponseUtil.createSuccessStatus());

        MDC.clear();
        return result;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indicates that the request was executed successfully"),
            @ApiResponse(responseCode = "500", description = "Indicates that there is a server error occurs during executing the request")})
    @PostMapping(value = "/refresh-token", consumes = "application/json",produces = "application/json")
    public ResponseWithBody<?> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        String traceId = UUID.randomUUID().toString();
        MDC.put(TRACE_ID, traceId);

        // Initialize result
        ResponseWithBody<TokenResponseDTO> result = new ResponseWithBody<>();

        // Get token
        TokenResponseDTO tokenResponseDTO = authenticationService.refreshToken(refreshTokenRequestDTO);

        result.setBody(tokenResponseDTO);
        result.setStatus(ResponseUtil.createSuccessStatus());

        MDC.clear();
        return result;
    }
}
