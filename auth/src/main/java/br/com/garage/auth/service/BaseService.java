package br.com.garage.auth.service;

import br.com.garage.auth.models.Role;
import br.com.garage.auth.models.Tenant;
import br.com.garage.auth.models.Usuario;
import br.com.garage.auth.repositories.RoleRepository;
import br.com.garage.auth.repositories.TenantRepository;
import br.com.garage.auth.repositories.UserRepository;
import br.com.garage.commons.enums.EnumStatus;
import br.com.garage.commons.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class BaseService {

    private static final String EMAIL_NAO_ENCONTRADO = "e-mail não encontrado";
    private static final String EMPRESA_NAO_ENCONTRADA = "empresa não encontrada";
    private static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado";
    private static final String ROLE_NAO_ENCONTRADA = "Role não encontrada";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private RoleRepository roleRepository;


    protected void salvarUsuario(Usuario usuario) {
        userRepository.save(usuario);
    }

    protected Usuario buscaUsuarioPorEmail(String email) throws UsernameNotFoundException {
        return userRepository.buscaPorEmail(email, EnumStatus.ATIVO)
                .orElseThrow(() -> new UsernameNotFoundException(EMAIL_NAO_ENCONTRADO));
    }

    protected Tenant buscarTenantPorId(UUID tenantId) throws NotFoundException {
        return tenantRepository.findById(tenantId)
                .orElseThrow(() -> new NotFoundException(EMPRESA_NAO_ENCONTRADA));
    }

    protected Usuario buscarUsuarioPorId(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(USUARIO_NAO_ENCONTRADO));
    }

    protected List<Usuario> buscarUsuariosPorTenant(Tenant tenant) {
        return userRepository.buscaPorTenant(EnumStatus.ATIVO, tenant);
    }

    protected Role buscarRolePorRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new NotFoundException(ROLE_NAO_ENCONTRADA));
    }

    protected void removeTenant(Tenant tenant) {
        tenantRepository.delete(tenant);
    }

    protected Tenant buscarTenantPorCpfCnpj(String cnpj) {
        Optional<Tenant> opt = tenantRepository.buscarPorCnpj(cnpj);
        return opt.orElse(null);
    }

    protected void salvarTenant(Tenant tenant) {
        tenantRepository.save(tenant);
    }
}
