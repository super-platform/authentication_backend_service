package com.ngtu.api.controllers.internal;

import com.ngtu.api.common.dto.response.ResponseWithBody;
import com.ngtu.api.common.utils.KeysGenerator;
import com.ngtu.api.common.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Key Generator", description = "Key generator Resources API")
@Slf4j
@RequiredArgsConstructor
public class KeyResources {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indicates that private key was generated successfully"),
            @ApiResponse(responseCode = "500", description = "Error")})
    @GetMapping(value = "/generate-secret-key", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWithBody<?>> getSecretKey(@Parameter String path) throws Exception{
        // Process generate private key
        String privateKey = KeysGenerator.generateKey(path);

        // Prepare response
        ResponseWithBody<?> response = ResponseWithBody.builder()
                .body(privateKey)
                .status(ResponseUtil.createSuccessStatus())
                .build();

        // Return
        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Indicates that pair key was generated successfully"),
            @ApiResponse(responseCode = "500", description = "Error")})
    @GetMapping(value = "/generate-pair-key", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWithBody<?>> getPairKey(@Parameter String path) throws Exception{
        // Process generate pair key
        List<String> pairs = KeysGenerator.generatePairKey(path);

        // Prepare response
        ResponseWithBody<?> response = ResponseWithBody.builder()
                .body(pairs)
                .status(ResponseUtil.createSuccessStatus())
                .build();

        // Return
        return ResponseEntity.ok(response);
    }
}
