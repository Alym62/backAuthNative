package org.auth.facul.controller;

import org.auth.facul.config.jwt.JwtService;
import org.auth.facul.dto.UsuarioDTO;
import org.auth.facul.entity.Usuario;
import org.auth.facul.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private final JwtService jwtService;

    private final AuthenticationManager authManager;

    private final UsuarioService usuarioService;

    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtService jwtService, AuthenticationManager authManager, UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody UsuarioDTO usuarioDTO) {
        var user = new Usuario();
        user.setUsername(usuarioDTO.getUsername());
        user.setPassword(passwordEncoder.encode(usuarioDTO.getUsername()));

        usuarioService.registrarUsuario(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO usuarioDTO) {
        // TODO: Não está conseguindo fazer login.
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                usuarioDTO.getUsername(), usuarioDTO.getPassword()
        ));

        var user = usuarioService.loadUserByUsername(usuarioDTO.getUsername());

        return ResponseEntity.ok(jwtService.gerarToken(user));
    }
}
