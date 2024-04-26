package com.spring.loginroles.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultResponseDto {
    private String role;
    public DefaultResponseDto (String message) {
        this.message=message;
    }
    private String message;
}
