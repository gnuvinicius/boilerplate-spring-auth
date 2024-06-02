package br.com.garage.auth.service;

import br.com.garage.auth.models.Role;
import br.com.garage.auth.models.Usuario;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends BaseService {

	public void addRoleAdmin(Usuario usuario) {
		Role adminRole = buscarRolePorRoleName("ROLE_ADMIN");
		usuario.getRoles().add(adminRole);
	}

	public void addRoleCadastro(Usuario usuario) {
		Role adminRole = buscarRolePorRoleName("ROLE_USER");
		usuario.getRoles().add(adminRole);
	}
}
