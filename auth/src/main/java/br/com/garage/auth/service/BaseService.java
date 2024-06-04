package br.com.garage.auth.service;

import br.com.garage.auth.models.Usuario;
import br.com.garage.auth.repositories.UserRepository;
import br.com.garage.commons.enums.EnumStatus;
import br.com.garage.commons.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public abstract class BaseService {

    @Autowired
    private UserRepository userRepository;

    protected final <T> T requireNotEmpty(Optional<T> optional) {
        return optional.orElseThrow(() -> new NotFoundException("NOT FOUND"));
    }

    protected Usuario buscaUsuarioPorEmail(String email) throws UsernameNotFoundException {
        return requireNotEmpty(userRepository.buscaPorEmail(email, EnumStatus.ATIVO));
    }

}
