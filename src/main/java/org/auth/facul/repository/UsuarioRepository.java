package org.auth.facul.repository;

import org.auth.facul.entity.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
