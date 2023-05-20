package com.ngtu.api.controllers.internal;


import com.ngtu.api.common.dto.response.ResponseWithBody;
import com.ngtu.api.common.utils.ResponseUtil;
import com.ngtu.api.dto.TestDTO;
import com.ngtu.api.entities.Template;
import com.ngtu.api.repositories.mongo.TemplateRepository;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

import static com.ngtu.api.common.constant.CommonConstants.TRACE_ID;

@RestController
@RequestMapping("/v1")
@Tag(name = "Rest", description = "Rest Resources API")
@Slf4j
@RequiredArgsConstructor
public class RestResources {

    private final TemplateRepository templateRepository;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "500", description = "error")})
    @PostMapping(value = "/send-rest", consumes = "application/json",produces = "application/json")
    public ResponseEntity<ResponseWithBody<?>> sendRest(@RequestBody @Valid TestDTO testDTO){
        String traceId = UUID.randomUUID().toString();
        MDC.put(TRACE_ID, traceId);
        log.info("tutruong");
        Optional<Template> optionalTemplate = templateRepository.findTemplateByTest(testDTO.getTest());
        String result = "";
        if(optionalTemplate.isPresent()){
            result = optionalTemplate.get().getTest();
        }
        MDC.clear();
        return ResponseEntity.ok(new ResponseWithBody(result, ResponseUtil.createSuccessStatus()));
    }
}
