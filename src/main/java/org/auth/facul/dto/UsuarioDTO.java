package org.auth.facul.dto;

import org.auth.facul.entity.enums.Role;

public record UsuarioDTO (
        String username,
        String password,
        Role role
){ }
