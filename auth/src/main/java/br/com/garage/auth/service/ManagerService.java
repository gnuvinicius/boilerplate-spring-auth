package br.com.garage.auth.service;

import br.com.garage.auth.interfaces.rest.dtos.TenantRequestDto;
import br.com.garage.auth.interfaces.rest.dtos.UsuarioRequestDto;
import br.com.garage.auth.interfaces.rest.dtos.UsuarioResponseDto;
import br.com.garage.auth.models.Tenant;
import br.com.garage.auth.models.Usuario;
import br.com.garage.auth.repositories.TenantRepository;
import br.com.garage.auth.repositories.UserRepository;
import br.com.garage.commons.enums.EnumStatus;
import br.com.garage.commons.exceptions.BusinessException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerService extends BaseService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @Autowired
    private ModelMapper mapper;

    public UsuarioResponseDto cadastraUsuario(UsuarioRequestDto dto) {
        //TODO
        buscaUsuarioPorEmail(dto.getEmail());

        authService.validPasswordPolicies(dto.getPassword());
        var usuario = new Usuario(dto, passwordEncoder.encode(dto.getPassword()), authService.getTenant());
        if (dto.isAdmin()) {
            roleService.addRoleAdmin(usuario);
        }
        roleService.addRoleCadastro(usuario);
        userRepository.save(usuario);
        //if (dto.isSendWelcomeEmail()) {
        // emailService.sendMail(usuario.getEmail(), "", welcomeEmail());
        //}
        return mapper.map(usuario, UsuarioResponseDto.class);
    }

    public List<UsuarioResponseDto> listaTodosUsuarios() {
        List<Usuario> usuarios = userRepository.buscaPorTenant(EnumStatus.ATIVO, authService.getTenant());
        return usuarios.stream().map(u -> mapper.map(u, UsuarioResponseDto.class)).collect(Collectors.toList());
    }

    public void cadastraUsuarioAdminDefault(TenantRequestDto dto) {
        try {
            if (tenantRepository.buscarPorCnpj(dto.getCnpj()).isPresent()) {
                throw new BusinessException("empresa ja cadastrada!");
            }

            Tenant tenant = tenantRepository.save(dto.toModel());

            validaUsuarioInexistente(dto, tenant);

            var usuario = new Usuario(dto.getAdmin(), passwordEncoder.encode(dto.getAdmin().getPassword()), tenant);
            roleService.addRoleAdmin(usuario);
            roleService.addRoleCadastro(usuario);
            userRepository.save(usuario);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    private void validaUsuarioInexistente(TenantRequestDto dto, Tenant tenant) {
        if (userRepository.buscaPorEmail(dto.getAdmin().getEmail(), EnumStatus.ATIVO).isPresent()) {
            tenantRepository.delete(tenant);
            throw new BusinessException("já existe um usuario com esse e-mail");
        }
    }
}
