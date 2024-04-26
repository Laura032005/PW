package com.spring.loginroles.Security;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.spring.loginroles.Model.Rol;
import com.spring.loginroles.Model.Usuario;
import com.spring.loginroles.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUsersDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Collection<GrantedAuthority> mapToAuthorities(Rol rol) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        // Mapear los roles a autoridades
        GrantedAuthority roleAuthorities = new SimpleGrantedAuthority(rol.getName());
        // Agregar las autoridades de los roles a la lista final
        authorities.add(roleAuthorities);
        return authorities;
    }

    //Método para traernos un usuario con todos sus datos por medio de sus email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new User(email, usuario.getPassword(), mapToAuthorities(usuario.getRol()));
    }
}
