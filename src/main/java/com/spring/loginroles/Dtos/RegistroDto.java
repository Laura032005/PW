package com.spring.loginroles.Dtos;

import lombok.Data;

@Data
public class RegistroDto {
    private String email;
    private String password;
    private String rol;
}
