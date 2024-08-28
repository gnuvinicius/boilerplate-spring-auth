package br.com.garage.auth.interfaces.rest;

import br.com.garage.auth.config.ResponseErrorBody;
import br.com.garage.auth.config.security.TokenService;
import br.com.garage.auth.interfaces.rest.dtos.RequestRefreshPasswordDto;
import br.com.garage.auth.interfaces.rest.dtos.TokenDto;
import br.com.garage.auth.interfaces.rest.dtos.UserLoginRequestDto;
import br.com.garage.auth.models.Usuario;
import br.com.garage.auth.repositories.UserRepository;
import br.com.garage.auth.service.UserService;
import br.com.garage.commons.enums.EnumStatus;
import br.com.garage.commons.exceptions.BusinessException;
import br.com.garage.commons.utils.Utils;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@Validated
@RequestMapping(value = "/auth/api/v1")
public class AuthResource {

    public static final String USUARIO_OU_SENHA_INVALIDOS = "Usuario ou senha invalidos";
    public static final String SUA_EMPRESA_ESTA_INATIVA = "sua empresa está inativa!";
    public static final String BEARER = "Bearer";
    private final UserService service;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public AuthResource(UserService service,
                        AuthenticationManager authenticationManager,
                        TokenService tokenService,
                        UserRepository userRepository) {
        this.service = service;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @ApiResponse(responseCode = "200", description = "Found the book",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TokenDto.class))})
    @PostMapping(value = "login")
    public ResponseEntity<?> auth(@RequestBody UserLoginRequestDto dto) throws AuthenticationException {

        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(dto.email, dto.password);
        try {
            var authenticate = authenticationManager.authenticate(login);
            String token = tokenService.buildToken(authenticate);
            Usuario usuario = Utils.requireNotEmpty(userRepository.buscaPorEmail(dto.email, EnumStatus.ATIVO));
            TokenDto tokenDto = new TokenDto(token, BEARER, null);
            if (usuario.getTenant() != null) {
                if (usuario.getTenant().getStatus().equals(EnumStatus.INATIVO)) {
                    throw new BusinessException(SUA_EMPRESA_ESTA_INATIVA);
                }

                tokenDto.tenantName = usuario.getTenant().getNome();
            }
            usuario.atualizaDataUltimoLogin();
            userRepository.save(usuario);

            return ResponseEntity.ok(tokenDto);
        } catch (InternalAuthenticationServiceException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseErrorBody(LocalDateTime.now(), USUARIO_OU_SENHA_INVALIDOS, HttpStatus.BAD_REQUEST.value()));
        }
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

}
