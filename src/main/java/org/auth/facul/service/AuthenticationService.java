package org.auth.facul.service;

import org.auth.facul.dto.UsuarioDTOLogin;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthenticationService extends UserDetailsService {
    String obterToken(UsuarioDTOLogin dtoLogin);

    String validaTokenJwt(String token);
}
