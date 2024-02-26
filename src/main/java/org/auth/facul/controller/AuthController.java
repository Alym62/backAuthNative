package org.auth.facul.controller;

import org.auth.facul.dto.UsuarioDTO;
import org.auth.facul.dto.UsuarioDTOLogin;
import org.auth.facul.service.AuthenticationService;
import org.auth.facul.service.impl.UsuarioServiceImpl;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthenticationManager authManager;

    private final UsuarioServiceImpl service;

    private final AuthenticationService authService;

    public AuthController(AuthenticationManager authManager, UsuarioServiceImpl service, AuthenticationService authService) {
        this.authService = authService;
        this.authManager = authManager;
        this.service = service;
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> registrar(@RequestBody UsuarioDTO dto) {
        var novoEntity = service.salvar(dto);
        return new ResponseEntity<>(novoEntity, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsuarioDTOLogin dtoLogin) {
        var usuarioAuthenticationToken =
                new UsernamePasswordAuthenticationToken(dtoLogin.username(), dtoLogin.password());

        authManager.authenticate(usuarioAuthenticationToken);

        var token = authService.obterToken(dtoLogin);

        System.out.println(token);

        return new ResponseEntity<>(token, HttpStatusCode.valueOf(200));
    }
}
