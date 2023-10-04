package com.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequestDTO {
    private String username;
    private String password;

    @Override
    public String toString(){
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
