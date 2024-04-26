package com.spring.loginroles.service.Impl;

import com.spring.loginroles.Repository.RolRepository;
import com.spring.loginroles.Repository.UsuarioRepository;
import com.spring.loginroles.Security.JwtGenerador;
import com.spring.loginroles.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    RolRepository rolRepository;
    @Autowired
    JwtGenerador jwtGenerador;

    @Override
    public void eliminarUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }


}

