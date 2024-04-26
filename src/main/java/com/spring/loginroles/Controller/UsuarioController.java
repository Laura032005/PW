package com.spring.loginroles.Controller;

import java.util.List;
import java.util.Optional;

import com.spring.loginroles.Repository.UsuarioRepository;
import com.spring.loginroles.Security.JwtGenerador;
import com.spring.loginroles.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/V1/usuario")
@CrossOrigin(origins = "http://localhost:5135")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    JwtGenerador jwtGenerador;


    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<String> eliminarUsuario(HttpServletRequest req, @PathVariable Integer id) {
        try { //extraer el token de autorizacion del encabezado de la solicitud
            String token = req.getHeader("Authorization").substring(7);
            //verificar si el usuario que realiza la solicitud es una admin
            if (jwtGenerador.obtenerRolDeJwt(token).equals("ADMIN")) { //si es admin. llama al servicio para eliminar al usuario con el ID proporcionada
                usuarioService.eliminarUsuario(id);
                return ResponseEntity.ok("Usuario eliminado correctamente");
            } else { // si no es admin, devolver un error de permiso denegado
                // Devolver un mensaje de error adecuado en caso de excepción
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo los administradores pueden eliminar usuarios");
            }
        } catch (Exception e) {
            //capturar cualquier excepcion que pueda ocurrir en el proceso devolver un error adecuado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar usuario: " + e.getMessage());
        }
    }

}
