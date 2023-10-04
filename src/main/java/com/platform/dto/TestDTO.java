package com.platform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TestDTO {

    @NotBlank(message = "test is mandatory")
    private String test;

    @Override
    public String toString(){
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
