package br.com.garage.auth.interfaces.rest;

import br.com.garage.auth.interfaces.rest.dtos.UsuarioResponseDto;
import br.com.garage.auth.interfaces.rest.dtos.TenantRequestDto;
import br.com.garage.auth.interfaces.rest.dtos.UsuarioRequestDto;
import br.com.garage.auth.models.Tenant;
import br.com.garage.auth.service.ManagerService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController()
@RequestMapping("/notes/auth/api/v1/manager")
@Log4j2
public class ManagerController {

	private final ManagerService service;

	public ManagerController(ManagerService service) {
		this.service = service;
	}

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
		service.arquiva(id);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/empresas")
	public ResponseEntity<?> cadastra(@RequestBody TenantRequestDto request) throws Exception {
		log.info("cadastrando empresa: {}", request.getNome());
		Tenant tenant = service.cadastraTenant(request);

		URI uri = new URI(tenant.getId().toString());
		return ResponseEntity.created(uri).build();
	}
}
