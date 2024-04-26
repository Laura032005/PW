package com.spring.loginroles.service;


import com.spring.loginroles.Dtos.DefaultResponseDto;
import com.spring.loginroles.Dtos.LoginDto;
import com.spring.loginroles.Dtos.RegistroDto;
import com.spring.loginroles.Dtos.ServiceResponseDto;
import com.spring.loginroles.Model.Usuario;

import java.util.List;

public interface AuthService {

    ServiceResponseDto<DefaultResponseDto> login(LoginDto loginDto);
    ServiceResponseDto<DefaultResponseDto> usuarioRegister(RegistroDto registroDto);
    ServiceResponseDto<DefaultResponseDto> adminRegister(RegistroDto registroDto);
    ServiceResponseDto<DefaultResponseDto> ModificarUsuarios(Integer id, RegistroDto registroDto);
    List<Usuario> userListado(String token) throws Exception;

}

