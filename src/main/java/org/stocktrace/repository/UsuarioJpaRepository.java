package org.stocktrace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stocktrace.model.Usuario;

import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail (String email);

    Boolean existsByEmail(String email);

}
