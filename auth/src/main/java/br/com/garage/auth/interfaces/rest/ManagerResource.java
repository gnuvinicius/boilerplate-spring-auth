package br.com.garage.auth.interfaces.rest;

import br.com.garage.auth.interfaces.rest.dtos.UsuarioResponseDto;
import br.com.garage.auth.interfaces.rest.dtos.TenantRequestDto;
import br.com.garage.auth.interfaces.rest.dtos.UsuarioRequestDto;
import br.com.garage.auth.repositories.UserRepository;
import br.com.garage.auth.service.ManagerService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
@RequestMapping("/auth/api/v1/manager")
@Log4j2
public class ManagerResource {

    @Autowired
    private ManagerService service;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/usuarios")
    public ResponseEntity<?> findAllUsersByCompany() {
        return ResponseEntity.ok(service.listaTodosUsuarios());
    }

    @PostMapping("/usuarios")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UsuarioResponseDto> cadastra(@RequestBody @Valid UsuarioRequestDto request)
            throws Exception {
        return ResponseEntity.ok(service.cadastraUsuario(request));
    }

    @DeleteMapping("/usuario")
    public ResponseEntity<?> arquiva(@RequestParam(name = "id") UUID id) {
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
        service.cadastraUsuarioAdminDefault(request);
        return ResponseEntity.ok().build();
    }
}
