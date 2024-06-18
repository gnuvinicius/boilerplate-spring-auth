package br.com.garage.auth.interfaces.rest;

import br.com.garage.auth.interfaces.rest.dtos.TenantRequestDto;
import br.com.garage.auth.models.Tenant;
import br.com.garage.auth.models.Usuario;
import br.com.garage.auth.repositories.TenantRepository;
import br.com.garage.auth.repositories.UserRepository;
import br.com.garage.commons.enums.EnumStatus;
import br.com.garage.commons.exceptions.BusinessException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/auth/api/v1/manager")
@Log4j2
public class TenantResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TenantRepository tenantRepository;

    @PersistenceContext
    private EntityManager manager;

    @PostMapping("/tenants")
    public ResponseEntity<?> cadastra(@RequestBody TenantRequestDto request) throws Exception {
        log.info("cadastrando empresa: {}", request.getNome());

        try {
            if (tenantRepository.buscarPorCnpj(request.getCnpj()).isPresent()) {
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
}
