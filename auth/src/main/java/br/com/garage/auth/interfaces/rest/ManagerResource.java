package br.com.garage.auth.interfaces.rest;

import br.com.garage.auth.interfaces.rest.dtos.TenantRequestDto;
import br.com.garage.auth.interfaces.rest.dtos.UsuarioRequestDto;
import br.com.garage.auth.interfaces.rest.dtos.UsuarioResponseDto;
import br.com.garage.auth.models.Tenant;
import br.com.garage.auth.models.Usuario;
import br.com.garage.auth.repositories.TenantRepository;
import br.com.garage.auth.repositories.UserRepository;
import br.com.garage.auth.service.UserService;
import br.com.garage.commons.enums.EnumStatus;
import br.com.garage.commons.exceptions.BusinessException;
import br.com.garage.commons.utils.userInfo.UserAuthInfo;
import br.com.garage.commons.utils.userInfo.UserInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth/api/v1/manager")
@Log4j2
public class ManagerResource {

    @PersistenceContext
    private final EntityManager manager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final TenantRepository tenantRepository;
    private final ModelMapper mapper;

    public ManagerResource(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UserService userService,
                           EntityManager manager,
                           TenantRepository tenantRepository,
                           ModelMapper mapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.manager = manager;
        this.tenantRepository = tenantRepository;
        this.mapper = mapper;
    }

    @PostMapping("/tenants")
    public ResponseEntity<?> cadastra(@Valid @RequestBody TenantRequestDto request) throws Exception {
        log.info("cadastrando empresa: {}", request.getNome());

        try {
            if (tenantRepository.buscarPorCnpj(request.getCpfCnpj()).isPresent()) {
                throw new BusinessException("empresa ja cadastrada!");
            }

            if (userRepository.buscaPorEmail(request.getAdmin().getEmail(), EnumStatus.ATIVO).isPresent()) {
                throw new BusinessException("já existe um usuario com esse e-mail");
            }

            Tenant tenant = tenantRepository.save(request.toModel());

            var usuario = new Usuario(request.getAdmin(), tenant.getId(), passwordEncoder);
            usuario.addRoles(manager, new String[]{"ROLE_ADMIN", "ROLE_USER"});

            try {
                userRepository.save(usuario);
            } catch (Exception e) {
                tenantRepository.delete(tenant);
                throw e;
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/usuarios")
    public List<UsuarioResponseDto> findAllUsersByCompany(@UserInfo UserAuthInfo userInfo) {
        return userRepository
                .buscaPorTenant(EnumStatus.ATIVO, userInfo.getTenantId())
                .stream()
                .map(u -> mapper.map(u, UsuarioResponseDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioResponseDto> cadastra(@UserInfo UserAuthInfo userInfo, @RequestBody @Valid UsuarioRequestDto request)
            throws Exception {

        Optional<Usuario> opt = userRepository.buscaPorEmail(request.getEmail(), EnumStatus.ATIVO);
        if (opt.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        userService.validPasswordPolicies(request.getPassword());
        var usuario = new Usuario(request, userInfo.getTenantId(), passwordEncoder);
        if (request.isAdmin()) {
            usuario.addRoles(manager, new String[]{"ROLE_ADMIN"});
        }
        usuario.addRoles(manager, new String[]{"ROLE_USER"});
        userRepository.save(usuario);

        var response = mapper.map(usuario, UsuarioResponseDto.class);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/usuarios")
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

}
