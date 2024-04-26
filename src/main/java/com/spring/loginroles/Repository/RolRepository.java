package com.spring.loginroles.Repository;

import com.spring.loginroles.Model.Rol;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository

public interface RolRepository extends org.springframework.data.jpa.repository.JpaRepository<Rol,Integer>{
    Optional<Rol> findByName(String name);
}
