package br.com.garage.commons.models;

import br.com.garage.commons.enums.EnumStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@MappedSuperclass
public abstract class AggregateRoot {

	@Id
	protected UUID id;

//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "tenant_id", nullable = false)
//	public Tenant tenant;

	protected LocalDateTime criadoEm;
	protected LocalDateTime atualizadoEm;
	protected EnumStatus status;

	protected AggregateRoot() {
		this.id = UUID.randomUUID();
		this.criadoEm = LocalDateTime.now();
		this.atualizadoEm = LocalDateTime.now();
		this.status = EnumStatus.ATIVO;
	}
}
