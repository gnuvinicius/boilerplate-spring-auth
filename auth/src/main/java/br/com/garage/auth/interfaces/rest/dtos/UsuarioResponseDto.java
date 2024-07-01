package br.com.garage.auth.interfaces.rest.dtos;

import br.com.garage.commons.enums.EnumStatus;
import br.com.garage.auth.models.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class UsuarioResponseDto {

	private UUID id;
	private EnumStatus status;
	private String nome;
	private String email;
	private TenantDto tenant;
	private Set<Role> roles = new HashSet<>();
	private LocalDateTime ultimoAcesso;
	private LocalDateTime atualizadoEm;
}
