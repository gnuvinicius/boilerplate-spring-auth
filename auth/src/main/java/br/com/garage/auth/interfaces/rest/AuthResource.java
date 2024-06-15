package br.com.garage.auth.interfaces.rest;

import br.com.garage.auth.config.security.TokenService;
import br.com.garage.auth.models.Usuario;
import br.com.garage.auth.repositories.UserRepository;
import br.com.garage.commons.enums.EnumStatus;
import br.com.garage.commons.exceptions.BusinessException;
import br.com.garage.commons.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import br.com.garage.auth.interfaces.rest.dtos.UserLoginRequestDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@Validated
@RequestMapping(value = "/auth/api/v1")
public class AuthResource {

    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @ApiResponse(responseCode = "200", description = "Found the book",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TokenDto.class))})
    @PostMapping(value = "login")
    public ResponseEntity<?> auth(@RequestBody UserLoginRequestDto dto) throws AuthenticationException {

        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(dto.email, dto.password);
        var authenticate = authenticationManager.authenticate(login);
        String token = tokenService.buildToken(authenticate);
        Usuario usuario = Utils.requireNotEmpty(userRepository.buscaPorEmail(dto.email, EnumStatus.ATIVO));
        TokenDto tokenDto = new TokenDto(token, "Bearer", null);
        if (usuario.getTenant() != null) {
            if (usuario.getTenant().getStatus().equals(EnumStatus.INATIVO)) {
                throw new BusinessException("Tenant está inativa!");
            }

            tokenDto.tenantName = usuario.getTenant().getNome();
        }
        usuario.atualizaDataUltimoLogin();
        userRepository.save(usuario);

        return ResponseEntity.ok(tokenDto);
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
