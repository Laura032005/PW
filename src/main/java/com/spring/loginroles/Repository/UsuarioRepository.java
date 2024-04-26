package com.spring.loginroles.Repository;

import com.spring.loginroles.Model.Usuario;
import java.util.Optional;
import org.springframework.stereotype.Repository;
@Repository

public interface UsuarioRepository extends org.springframework.data.jpa.repository.JpaRepository<Usuario,Integer>{

    Optional<Usuario> findByEmail(String email);
    Boolean existsByEmail(String email);
}
