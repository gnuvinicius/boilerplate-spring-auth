package br.com.garage.auth.service;

import br.com.garage.auth.models.Role;
import br.com.garage.auth.models.Usuario;
import br.com.garage.auth.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends BaseService {

	@Autowired
	private RoleRepository roleRepository;

	public void addRoleAdmin(Usuario usuario) {
		Role adminRole = requireNotEmpty(roleRepository.findByRoleName("ROLE_ADMIN"));
		usuario.getRoles().add(adminRole);
	}

	public void addRoleCadastro(Usuario usuario) {
		Role adminRole = requireNotEmpty(roleRepository.findByRoleName("ROLE_USER"));
		usuario.getRoles().add(adminRole);
	}
}
