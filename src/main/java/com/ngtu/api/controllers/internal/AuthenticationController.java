package com.ngtu.api.controllers.internal;

import com.ngtu.api.common.dto.response.ResponseWithBody;
import com.ngtu.api.common.utils.ResponseUtil;
import com.ngtu.api.dto.TokenRequestDTO;
import com.ngtu.api.dto.TokenResponseDTO;
import com.ngtu.api.service.AuthenticationService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.ngtu.api.common.constant.CommonConstants.TRACE_ID;

@RestController
@RequestMapping("/v1")
@Tag(name = "Authentication", description = "Authentication Resources API")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "500", description = "error")})
    @PostMapping(value = "/token", consumes = "application/json",produces = "application/json")
    public ResponseEntity<ResponseWithBody<?>> getToken(@RequestBody TokenRequestDTO tokenRequestDTO){
        String traceId = UUID.randomUUID().toString();
        MDC.put(TRACE_ID, traceId);

        // Initialize result
        ResponseWithBody<TokenResponseDTO> result = new ResponseWithBody<>();

        // Get token
        TokenResponseDTO tokenResponseDTO = authenticationService.getToken(tokenRequestDTO);
        if(ObjectUtils.isNotEmpty(tokenResponseDTO) && StringUtils.isNotBlank(tokenResponseDTO.getAccessToken())){
            result.setBody(tokenResponseDTO);
            result.setStatus(ResponseUtil.createSuccessStatus());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        MDC.clear();
        return ResponseEntity.ok(new ResponseWithBody(null, ResponseUtil.createFailedStatus()));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "500", description = "error")})
    @PostMapping(value = "/refresh-token", consumes = "application/json",produces = "application/json")
    public ResponseEntity<ResponseWithBody<?>> refreshToken(@RequestBody TokenRequestDTO tokenRequestDTO){
        String traceId = UUID.randomUUID().toString();
        MDC.put(TRACE_ID, traceId);

        // Initialize result
        ResponseWithBody<TokenResponseDTO> result = new ResponseWithBody<>();

        // Get token
        TokenResponseDTO tokenResponseDTO = authenticationService.refreshToken(tokenRequestDTO);
        if(ObjectUtils.isNotEmpty(tokenResponseDTO) && StringUtils.isNotBlank(tokenResponseDTO.getAccessToken())){
            result.setBody(tokenResponseDTO);
            result.setStatus(ResponseUtil.createSuccessStatus());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        MDC.clear();
        return ResponseEntity.ok(new ResponseWithBody(null, ResponseUtil.createFailedStatus()));
    }
}
