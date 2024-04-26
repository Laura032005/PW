package com.spring.loginroles.service.Impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.spring.loginroles.Dtos.DefaultResponseDto;
import com.spring.loginroles.Dtos.LoginDto;
import com.spring.loginroles.Dtos.RegistroDto;
import com.spring.loginroles.Dtos.ServiceResponseDto;
import com.spring.loginroles.Messages.Message;
import com.spring.loginroles.Model.Rol;
import com.spring.loginroles.Model.Usuario;
import com.spring.loginroles.Repository.RolRepository;
import com.spring.loginroles.Repository.UsuarioRepository;
import com.spring.loginroles.Security.JwtGenerador;
import com.spring.loginroles.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtGenerador jwtGenerador;
    @Autowired
    private RolRepository rolRepository;

    @Override
    public ServiceResponseDto<DefaultResponseDto> login(LoginDto loginDto) {
        try {
            Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(loginDto.getEmail());
            if (optionalUsuario.isPresent() && passwordEncoder.matches(loginDto.getPassword(), optionalUsuario.get().getPassword())) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
                var rol = optionalUsuario.get().getRol().getName();
                DefaultResponseDto responseDto = new DefaultResponseDto(jwtGenerador.generarToken(authentication, rol));
                responseDto.setRole(rol); // Establecer el rol en el objeto de respuesta
                return new ServiceResponseDto<>(responseDto, HttpStatus.OK);
            } else {
                return new ServiceResponseDto<>(new DefaultResponseDto(Message.MENSAJE_ERROR_LOGIN), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ServiceResponseDto<>(new DefaultResponseDto(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ServiceResponseDto<DefaultResponseDto> adminRegister(RegistroDto registroDto) {
        try {

            if (usuarioRepository.existsByEmail(registroDto.getEmail())) {
                return new ServiceResponseDto<>(new DefaultResponseDto(Message.MENSAJE_ERROR_EMAIL), HttpStatus.BAD_REQUEST);
            }

            Optional<Rol> optionalRoles = rolRepository.findByName("ADMIN");
            if (optionalRoles.isPresent()) {
                Usuario usuario = new Usuario();
                usuario.setPassword(passwordEncoder.encode(registroDto.getPassword()));
                usuario.setEmail(registroDto.getEmail());
                usuario.setRol(optionalRoles.get());
                usuarioRepository.save(usuario);
                return new ServiceResponseDto<>(
                        new DefaultResponseDto(String.format(Message.MENSAJE_EXITOSO_REGISTRO, "ADMIN")), HttpStatus.OK);
            } else
                return new ServiceResponseDto<>(new DefaultResponseDto(Message.MENSAJE_ERROR_ROL), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ServiceResponseDto<>(new DefaultResponseDto(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public ServiceResponseDto<DefaultResponseDto> usuarioRegister(RegistroDto registroDto) {
        try {
            // Verificar si el correo electrónico ya está en uso
            if (usuarioRepository.existsByEmail(registroDto.getEmail())) {
                return new ServiceResponseDto<>(new DefaultResponseDto(Message.MENSAJE_ERROR_EMAIL), HttpStatus.BAD_REQUEST);
            }
            // Buscar el rol "Alcalde" por su nombre
            Optional<Rol> alcaldeRolOptional = rolRepository.findByName("USUARIO");
            if (alcaldeRolOptional.isEmpty()) {
                return new ServiceResponseDto<>(new DefaultResponseDto(Message.MENSAJE_ERROR_ROL),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            // Crear un nuevo usuario con el rol "Alcalde"
            Usuario nuevoAlcalde = new Usuario();
            nuevoAlcalde.setEmail(registroDto.getEmail());
            nuevoAlcalde.setPassword(passwordEncoder.encode(registroDto.getPassword()));
            nuevoAlcalde.setRol(alcaldeRolOptional.get()); // Utilizar el objeto Rol obtenido
            usuarioRepository.save(nuevoAlcalde);
            return new ServiceResponseDto<>(new DefaultResponseDto(String.format(Message.MENSAJE_EXITOSO_REGISTRO, "USUARIO")),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ServiceResponseDto<>(new DefaultResponseDto(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public ServiceResponseDto<DefaultResponseDto> ModificarUsuarios(Integer id, RegistroDto registroDto) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (!optionalUsuario.isPresent()) {
            return new ServiceResponseDto<>(new DefaultResponseDto(Message.MENSAJE_ERROR_ID_USUARIO), HttpStatus.BAD_REQUEST);
        }

        Usuario usuarioExistente = optionalUsuario.get();
        String rol = usuarioExistente.getRol().getName();
        if (!rol.equals("ADMIN") && !rol.equals("USUARIO")) {
            return new ServiceResponseDto<>(new DefaultResponseDto(Message.MENSAJE_ERROR_PERMISO), HttpStatus.FORBIDDEN);
        }

        // Verificar si el usuario autenticado es un alcalde
        if (!esAdminAutenticado()) {
            return new ServiceResponseDto<>(new DefaultResponseDto(Message.MENSAJE_ERROR_PERMISO), HttpStatus.FORBIDDEN);
        }

        // Verificar si el alcalde tiene permiso para actualizar este usuario
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Optional<Usuario> optionalUsuarioAutenticado = usuarioRepository.findByEmail(username);
        if (!optionalUsuarioAutenticado.isPresent()) {
            return new ServiceResponseDto<>(new DefaultResponseDto("No se encontró el usuario autenticado"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // Omitir la actualización del rol en el registroDto
        registroDto.setRol(rol);

        if (!registroDto.getEmail().equals(usuarioExistente.getEmail()) && usuarioRepository.existsByEmail(registroDto.getEmail())) {
            return new ServiceResponseDto<>(new DefaultResponseDto(Message.MENSAJE_ERROR_EMAIL), HttpStatus.BAD_REQUEST);
        }

        // Actualizar los detalles del usuario existente
        usuarioExistente.setEmail(registroDto.getEmail());
        usuarioRepository.save(usuarioExistente);

        return new ServiceResponseDto<>(new DefaultResponseDto(Message.MENSAJE_EXITOSO_ACTUALIZADO), HttpStatus.OK);
    }
    @Override
    public List<Usuario> userListado(String token) throws Exception {
        if (jwtGenerador.validarToken(token)) {
            return usuarioRepository.findAll();
        } else {
            throw new Exception("Token inválido");
        }
    }
    private boolean esAdminAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
    }

}