package br.com.garage.auth.service;

import br.com.garage.auth.interfaces.rest.dtos.TenantRequestDto;
import br.com.garage.auth.interfaces.rest.dtos.UsuarioRequestDto;
import br.com.garage.auth.interfaces.rest.dtos.UsuarioResponseDto;
import br.com.garage.auth.models.Tenant;
import br.com.garage.auth.models.Usuario;
import br.com.garage.commons.exceptions.BusinessException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ManagerService extends BaseService {

    @Autowired
    private RoleService roleService;

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
        var usuario = new Usuario(dto, passwordEncoder.encode(dto.getPassword()), authService.getCompanyByUserLogged());
        if (dto.isAdmin()) {
            roleService.addRoleAdmin(usuario);
        }
        roleService.addRoleCadastro(usuario);
        salvarUsuario(usuario);
        //if (dto.isSendWelcomeEmail()) {
        // emailService.sendMail(usuario.getEmail(), "", welcomeEmail());
        //}
        return mapper.map(usuario, UsuarioResponseDto.class);
    }

    public void arquiva(UUID id) {
        Usuario usuario = buscarUsuarioPorId(id);
        usuario.inativa();
        salvarUsuario(usuario);
    }

    public List<UsuarioResponseDto> listaTodosUsuarios() {
        List<Usuario> usuarios = buscarUsuariosPorTenant(authService.getCompanyByUserLogged());
        return usuarios.stream().map(u -> mapper.map(u, UsuarioResponseDto.class)).collect(Collectors.toList());
    }

    public Tenant cadastraTenant(TenantRequestDto dto) {
        try {
            var tenant = salvaNovaTenant(dto);
            cadastraUsuarioAdminDefault(dto.getAdmin(), tenant);
            return tenant;
        } catch (Exception ex) {
            throw new BusinessException("Não foi possivel concluir o cadastro da sua empresa: " + ex.getMessage());
        }
    }

    private Tenant salvaNovaTenant(TenantRequestDto dto) throws Exception {
        if (buscarTenantPorCpfCnpj(dto.getCnpj()) != null) {
            throw new BusinessException("empresa ja cadastrada!");
        }
        Tenant tenant = dto.toModel();
        salvarTenant(tenant);
        return tenant;
    }

    private void cadastraUsuarioAdminDefault(UsuarioRequestDto request, Tenant tenant) {
        try {
            if (buscaUsuarioPorEmail(request.getEmail()) != null) {
                removeTenant(tenant);
                throw new BusinessException("já existe um usuario com esse e-mail");
            }
            var usuario = new Usuario(request, passwordEncoder.encode(request.getPassword()), tenant);
            roleService.addRoleAdmin(usuario);
            roleService.addRoleCadastro(usuario);
            salvarUsuario(usuario);
        } catch (Exception ex) {
            removeTenant(tenant);
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
}
