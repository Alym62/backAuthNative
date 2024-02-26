package org.auth.facul.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.auth.facul.dto.UsuarioDTOLogin;
import org.auth.facul.entity.Usuario;
import org.auth.facul.repository.UsuarioRepository;
import org.auth.facul.service.AuthenticationService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UsuarioRepository repository;

    public AuthenticationServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsernameIgnoreCase(username);
    }

    @Override
    public String obterToken(UsuarioDTOLogin dtoLogin) {
        var usuario = repository.findByUsernameIgnoreCase(dtoLogin.username());
        return gerarTokenJwt(usuario);
    }

    private String gerarTokenJwt(Usuario usuario){
        try {
            Algorithm algorithm = HMAC256("secret-key");

            return JWT.create()
                    .withIssuer("facul")
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(gerarDataExpiracao())
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao tentar gerar o token: " + exception.getMessage());
        }
    }

    private Instant gerarDataExpiracao() {
        return LocalDateTime.now()
                .plusHours(1)
                .toInstant(ZoneOffset.of("-03:00"));
    }

    public String validaTokenJwt(String token) {
        try {
            Algorithm algorithm = HMAC256("secret-key");

            return JWT.require(algorithm)
                    .withIssuer("facul")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            return "";
        }
    }
}
