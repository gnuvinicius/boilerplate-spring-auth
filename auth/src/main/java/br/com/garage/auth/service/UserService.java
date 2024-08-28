package br.com.garage.auth.service;

import br.com.garage.auth.interfaces.rest.dtos.RequestRefreshPasswordDto;
import br.com.garage.auth.models.Tenant;
import br.com.garage.auth.models.Usuario;
import br.com.garage.auth.repositories.UserRepository;
import br.com.garage.commons.enums.EnumStatus;
import br.com.garage.commons.exceptions.BusinessException;
import br.com.garage.commons.exceptions.NotFoundException;
import br.com.garage.commons.utils.Utils;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Service
public class UserService implements UserDetailsService {

	private static final String EMPRESA_ESTA_NULO = "O campo empresa esta nulo";
	private static final Exception TOKEN_INVALIDO = null;

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return Utils.requireNotEmpty(userRepository.buscaPorEmail(email, EnumStatus.ATIVO));
	}

	public void updatePasswordByRefreshToken(RequestRefreshPasswordDto dto) throws BusinessException {
		var usuario = Utils.requireNotEmpty(userRepository.buscaPorEmail(dto.getEmail(), EnumStatus.ATIVO));

		if (!usuario.getTokenRefreshPassword().equals(dto.getTokenRefreshPassword())
				|| !usuario.isTokenRefreshPasswordValid()) {
			throw new BusinessException(TOKEN_INVALIDO);
		}
		validPasswordPolicies(dto.getNewPassword());
		usuario.alteraPassword(passwordEncoder.encode(dto.getNewPassword()));
		userRepository.save(usuario);
	}

	private Usuario getUSerLogged() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		var principal = (UserDetails) authentication.getPrincipal();

		return Utils.requireNotEmpty(userRepository.buscaPorEmail(principal.getUsername(), EnumStatus.ATIVO));
	}

	public Tenant getTenant() throws NotFoundException {
		if (getUSerLogged().getTenant() == null) {
			throw new IllegalArgumentException(EMPRESA_ESTA_NULO);
		}

		return getUSerLogged().getTenant();
	}

	public void validPasswordPolicies(String password) throws BusinessException {

		Matcher hasLetter = Pattern.compile("[a-z]").matcher(password);
		Matcher hasDigit = Pattern.compile("\\d").matcher(password);

		if (password.length() < 8 || !hasLetter.find() || !hasDigit.find()) {
			throw new BusinessException("password precisa ser mais complexo");
		}
	}

	public void solicitaAtualizarPassword(String email) {
		Usuario usuario = Utils.requireNotEmpty(userRepository.buscaPorEmail(email, EnumStatus.ATIVO));
		createRefreshToken(usuario);
		enviaEmailRefreshToken(usuario);
		userRepository.save(usuario);
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
		userRepository.save(usuario);
	}

}
