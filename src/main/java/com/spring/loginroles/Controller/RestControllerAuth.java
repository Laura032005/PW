package com.spring.loginroles.Controller;


import com.spring.loginroles.Dtos.DefaultResponseDto;
import com.spring.loginroles.Dtos.LoginDto;
import com.spring.loginroles.Dtos.RegistroDto;
import com.spring.loginroles.Dtos.ServiceResponseDto;
import com.spring.loginroles.Messages.Message;
import com.spring.loginroles.Model.Usuario;
import com.spring.loginroles.Repository.UsuarioRepository;
import com.spring.loginroles.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/V1/auth/")
@CrossOrigin(origins = "http://localhost:63342")
public class RestControllerAuth {

    @Autowired
    private AuthService authService;
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping("listar")
    public ResponseEntity<List<Usuario>> listarUsuarios(HttpServletRequest req) {
        try {
            String token = req.getHeader("Authorization").substring(7);
            List<Usuario> usuarios = authService.userListado(token);
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("login")
    public ResponseEntity<DefaultResponseDto> login(@RequestBody LoginDto loginDto) {
        ServiceResponseDto<DefaultResponseDto> response = authService.login(loginDto);
        return new ResponseEntity<>(response.getMessage(), response.getStatus());
    }

    @PostMapping("admin")
    public ServiceResponseDto<DefaultResponseDto> crearUsuario(@RequestBody RegistroDto registroDto) {
        return this.authService.adminRegister(registroDto);
    }

    @PostMapping("usuario")
    public ResponseEntity<DefaultResponseDto> crearAlcalde(@RequestBody RegistroDto registroDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"))) {
            ServiceResponseDto<DefaultResponseDto> response = authService.usuarioRegister(registroDto);
            return new ResponseEntity<>(response.getMessage(), response.getStatus());
        } else {
            return new ResponseEntity<>(new DefaultResponseDto(String.format(Message.MENSAJE_ERROR_AUTOIZADO, "ADMIN", "USUARIO")),
                    HttpStatus.FORBIDDEN);
        }
    }
    @PutMapping("/modificar/{id}")
    public ResponseEntity<DefaultResponseDto> ModificarUsuarios(@PathVariable Integer id, @RequestBody RegistroDto registroDto) {
        try {
            if (!esAdminAutenticado()) {
                return new ResponseEntity<>(new DefaultResponseDto(Message.MENSAJE_ERROR_PERMISO), HttpStatus.FORBIDDEN);
            }
            ServiceResponseDto<DefaultResponseDto> response = authService.ModificarUsuarios(id, registroDto);
            return new ResponseEntity<>(response.getMessage(), response.getStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(new DefaultResponseDto(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private boolean esAdminAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
    }
}

