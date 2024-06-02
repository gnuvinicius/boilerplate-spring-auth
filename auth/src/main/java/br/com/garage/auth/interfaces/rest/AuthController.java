package br.com.garage.auth.interfaces.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.garage.auth.service.AuthService;
import br.com.garage.auth.interfaces.rest.dtos.RequestRefreshPasswordDto;
import br.com.garage.auth.interfaces.rest.dtos.TokenDto;
import br.com.garage.auth.interfaces.rest.dtos.UserDto;
import br.com.garage.auth.interfaces.rest.dtos.UserLoginRequestDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@Validated
@RequestMapping(value = "/notes/auth/api/v1")
public class AuthController {

    @Autowired
    private AuthService service;

    @ApiResponse(responseCode = "200", description = "Found the book",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TokenDto.class))})
    @PostMapping(value = "login")
    public ResponseEntity<?> auth(@RequestBody UserLoginRequestDto dto) throws AuthenticationException {
        return ResponseEntity.ok(service.auth(dto));
    }

    @GetMapping(value = "/reset-password")
    public ResponseEntity<?> requestUpdatePassword(@RequestParam(name = "login") String login) throws Exception {
        service.solicitaAtualizarPassword(login);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/reset-password")
    public ResponseEntity<?> updatePasswordByRefreshToken(@RequestBody RequestRefreshPasswordDto dto) throws Exception {
        service.updatePasswordByRefreshToken(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam(name = "token") String token) throws Exception {
        UserDto user = service.validateToken(token);
        return ResponseEntity.ok(user);
    }
}
