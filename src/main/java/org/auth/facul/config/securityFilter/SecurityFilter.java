package org.auth.facul.config.securityFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.auth.facul.entity.Usuario;
import org.auth.facul.repository.UsuarioRepository;
import org.auth.facul.service.AuthenticationService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final AuthenticationService authService;

    private final UsuarioRepository repository;

    public SecurityFilter(AuthenticationService authService, UsuarioRepository repository) {
        this.authService = authService;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extrairTokenHeader(request);

        if (token != null){
            String username = authService.validaTokenJwt(token);
            Usuario usuario = repository.findByUsernameIgnoreCase(username);

            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String extrairTokenHeader(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");

        if (authHeader == null)
            return null;

        if (!authHeader.split(" ")[0].equals("Bearer"))
            return null;

        return authHeader.split(" ")[1];
    }
}
