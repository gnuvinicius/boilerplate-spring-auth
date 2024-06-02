package br.com.garage.auth.service;

import br.com.garage.auth.config.security.TokenService;
import br.com.garage.auth.interfaces.rest.dtos.RequestRefreshPasswordDto;
import br.com.garage.auth.interfaces.rest.dtos.TokenDto;
import br.com.garage.auth.interfaces.rest.dtos.UserDto;
import br.com.garage.auth.interfaces.rest.dtos.UserLoginRequestDto;
import br.com.garage.auth.models.Tenant;
import br.com.garage.auth.models.Usuario;
import br.com.garage.commons.enums.EnumStatus;
import br.com.garage.commons.exceptions.BusinessException;
import br.com.garage.commons.exceptions.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Log4j2
public class AuthService extends BaseService {

    private static final String EMPRESA_ESTA_NULO = "O campo empresa esta nulo";
    private static final Exception TOKEN_INVALIDO = null;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public TokenDto auth(UserLoginRequestDto dto) {

        var login = new UsernamePasswordAuthenticationToken(dto.email, dto.password);
        var authenticate = authenticationManager.authenticate(login);
        String token = tokenService.buildToken(authenticate);
        Usuario usuario = buscaUsuarioPorEmail(dto.email);
        TokenDto tokenDto = new TokenDto(token, "Bearer", null);
        if (usuario.getTenant() != null) {
            if (usuario.getTenant().getStatus().equals(EnumStatus.INATIVO)) {
                throw new BusinessException("Tenant está inativa!");
            }

            tokenDto.tenantName = usuario.getTenant().getNome();
        }
        usuario.atualizaDataUltimoLogin();
        salvarUsuario(usuario);
        return tokenDto;
    }

    public void updatePasswordByRefreshToken(RequestRefreshPasswordDto dto) throws BusinessException {
        var entity = buscaUsuarioPorEmail(dto.getEmail());

        if (!entity.getTokenRefreshPassword().equals(dto.getTokenRefreshPassword())
                || !entity.isTokenRefreshPasswordValid()) {
            throw new BusinessException(TOKEN_INVALIDO);
        }
        validPasswordPolicies(dto.getNewPassword());
        entity.alteraPassword(passwordEncoder.encode(dto.getNewPassword()));
        salvarUsuario(entity);
    }

    public Usuario getUSerLogged() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var principal = (UserDetails) authentication.getPrincipal();

        return buscaUsuarioPorEmail(principal.getUsername());
    }

    public Tenant getCompanyByUserLogged() throws NotFoundException {
        if (getUSerLogged().getTenant() == null) {
            throw new IllegalArgumentException(EMPRESA_ESTA_NULO);
        }

        return buscarTenantPorId(getUSerLogged().getTenant().getId());
    }

    public void validPasswordPolicies(String password) throws BusinessException {

        Matcher hasLetter = Pattern.compile("[a-z]").matcher(password);
        Matcher hasDigit = Pattern.compile("\\d").matcher(password);

        if (password.length() < 8 || !hasLetter.find() || !hasDigit.find()) {
            throw new BusinessException("password precisa ser mais complexo");
        }
    }

    public void solicitaAtualizarPassword(String email) {
        Usuario usuario = buscaUsuarioPorEmail(email);
        createRefreshToken(usuario);
        enviaEmailRefreshToken(usuario);
        salvarUsuario(usuario);
    }

    private void enviaEmailRefreshToken(Usuario usuario) {

        String str = "http://localhost:4200/auth/update-password" +
                ";email=" +
                usuario.getEmail() +
                ";refreshtoken=" +
                usuario.getTokenRefreshPassword();
        log.info("e-mail enviado com sucesso: {}", str);
    }

    private void createRefreshToken(Usuario usuario) {
        String token = passwordEncoder.encode(usuario.getEmail() + usuario.getPassword() + LocalDateTime.now());
        usuario.ativaRefreshToken(token);
        salvarUsuario(usuario);
    }

    public UserDto validateToken(String token) {
        Usuario usuario = buscaUsuarioPorEmail(tokenService.validateToken(token));
        return new UserDto(usuario.getId(), usuario.getEmail(), token);
    }


}
