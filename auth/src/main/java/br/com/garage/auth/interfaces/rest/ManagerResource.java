package br.com.garage.auth.interfaces.rest;

import br.com.garage.auth.interfaces.rest.dtos.UsuarioResponseDto;
import br.com.garage.auth.interfaces.rest.dtos.TenantRequestDto;
import br.com.garage.auth.interfaces.rest.dtos.UsuarioRequestDto;
import br.com.garage.auth.models.Tenant;
import br.com.garage.auth.models.Usuario;
import br.com.garage.auth.repositories.TenantRepository;
import br.com.garage.auth.repositories.UserRepository;
import br.com.garage.auth.service.AuthService;
import br.com.garage.auth.service.RoleService;
import br.com.garage.commons.enums.EnumStatus;
import br.com.garage.commons.exceptions.BusinessException;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/auth/api/v1/manager")
@Log4j2
public class ManagerResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/usuarios")
    public List<UsuarioResponseDto> findAllUsersByCompany() {
        return userRepository
                .buscaPorTenant(EnumStatus.ATIVO, authService.getTenant())
                .stream()
                .map(u -> mapper.map(u, UsuarioResponseDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/usuarios")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UsuarioResponseDto> cadastra(@RequestBody @Valid UsuarioRequestDto request)
            throws Exception {

        Optional<Usuario> opt = userRepository.buscaPorEmail(request.getEmail(), EnumStatus.ATIVO);
        if (opt.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        authService.validPasswordPolicies(request.getPassword());
        var usuario = new Usuario(request, passwordEncoder.encode(request.getPassword()), authService.getTenant());
        if (request.isAdmin()) {
            roleService.addRoleAdmin(usuario);
        }
        roleService.addRoleCadastro(usuario);
        userRepository.save(usuario);

        var response = mapper.map(usuario, UsuarioResponseDto.class);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/usuario")
    public ResponseEntity<?> arquiva(@RequestParam(name = "id") Long id) {
        return userRepository.findById(id)
                .map(usuario -> {
                    usuario.inativa();
                    userRepository.save(usuario);
                    log.info("desativando usuario: {}", usuario.getEmail());
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/empresas")
    public ResponseEntity<?> cadastra(@RequestBody TenantRequestDto request) throws Exception {
        log.info("cadastrando empresa: {}", request.getNome());
        try {
            if (tenantRepository.buscarPorCnpj(request.getCnpj()).isPresent()) {
                throw new BusinessException("empresa ja cadastrada!");
            }

            Tenant entity = request.toModel();
            Tenant tenant = tenantRepository.save(entity);

            if (userRepository.buscaPorEmail(request.getAdmin().getEmail(), EnumStatus.ATIVO).isPresent()) {
                tenantRepository.delete(tenant);
                throw new BusinessException("já existe um usuario com esse e-mail");
            }

            var usuario = new Usuario(request.getAdmin(), passwordEncoder.encode(request.getAdmin().getPassword()), tenant);
            roleService.addRoleAdmin(usuario);
            roleService.addRoleCadastro(usuario);
            userRepository.save(usuario);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
