package org.auth.facul.service.impl;

import org.auth.facul.dto.UsuarioDTO;
import org.auth.facul.entity.Usuario;
import org.auth.facul.repository.UsuarioRepository;
import org.auth.facul.service.UsuarioService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository repository;

    private final BCryptPasswordEncoder encoder;

    public UsuarioServiceImpl(UsuarioRepository repository, BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public UsuarioDTO salvar(UsuarioDTO dto) {

        Usuario entity = new Usuario(dto.username(), encoder.encode(dto.password()), dto.role());
        var novoUsuario = repository.save(entity);

        return new UsuarioDTO(novoUsuario.getUsername(), novoUsuario.getPassword(), novoUsuario.getRole());
    }
}
