package org.auth.facul.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.auth.facul.service.UsuarioService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final UsuarioService usuarioService;

    private final JwtService jwtService;

    public JwtRequestFilter(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return (request.getRequestURI().startsWith("/api/v1/auth"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String REQUEST_TOKEN = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (REQUEST_TOKEN != null && REQUEST_TOKEN.startsWith("Bearer ")) {
            token = REQUEST_TOKEN.substring(7);
            username = jwtService.extrairUsuario(token);
        } else {
            throw new RuntimeException("O token não começa com [Bearer]");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var usuario = usuarioService.loadUserByUsername(username);
            var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(usuario, null,
                    usuario.getAuthorities());

            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            filterChain.doFilter(request, response);
        }
    }
}
