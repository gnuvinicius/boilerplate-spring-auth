package br.com.garage.auth.interfaces.rest;

import br.com.garage.auth.interfaces.rest.dtos.UsuarioResponseDto;
import br.com.garage.auth.interfaces.rest.dtos.UsuarioRequestDto;
import br.com.garage.auth.models.Usuario;
import br.com.garage.auth.repositories.UserRepository;
import br.com.garage.auth.service.AuthService;
import br.com.garage.commons.enums.EnumStatus;
import br.com.garage.commons.utils.userInfo.UserAuthInfo;
import br.com.garage.commons.utils.userInfo.UserInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/auth/api/v1/manager")
@Log4j2
public class ManagerResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private ModelMapper mapper;

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

        authService.validPasswordPolicies(request.getPassword());
        var usuario = new Usuario(request, userInfo.getTenantId(), passwordEncoder);
        if (request.isAdmin()) {
            usuario.addRoles(manager, new String[]{"ROLE_ADMIN"});
        }
        usuario.addRoles(manager, new String[]{"ROLE_ADMIN"});
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
