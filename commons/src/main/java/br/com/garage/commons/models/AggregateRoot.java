package br.com.garage.commons.models;

import br.com.garage.commons.enums.EnumStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class AggregateRoot {

	protected LocalDateTime criadoEm;
	protected LocalDateTime atualizadoEm;
	protected EnumStatus status;

	protected AggregateRoot() {
		this.criadoEm = LocalDateTime.now();
		this.atualizadoEm = LocalDateTime.now();
		this.status = EnumStatus.ATIVO;
	}
}
