package br.com.garage.auth.service;

import br.com.garage.auth.repositories.UserRepository;
import br.com.garage.commons.enums.EnumStatus;
import br.com.garage.commons.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return Utils.requireNotEmpty(userRepository.buscaPorEmail(email, EnumStatus.ATIVO));
	}

}
