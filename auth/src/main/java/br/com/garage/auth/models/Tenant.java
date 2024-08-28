package br.com.garage.auth.models;

import java.time.LocalDateTime;

import org.springframework.util.Assert;

import br.com.garage.auth.interfaces.rest.dtos.TenantRequestDto;
import br.com.garage.commons.enums.EnumStatus;
import br.com.garage.commons.utils.AssertionConcern;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "tb_tenants")
public class Tenant {

	private static final String NULO_OU_VAZIO = "o campo %s não pode ser nulo ou vazio";

	@Id
	@SequenceGenerator(name = "seq_tenant", sequenceName = "tb_tenants_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tenant")
	private Long id;

	private EnumStatus status;
	private String nome;

	private String endereco;
	private String email;
	private LocalDateTime criadoEm;
	private LocalDateTime atualizadoEm;

	@Column(unique = true)
	private String cnpj;

	public Tenant(String nome, String endereco, String cnpj) throws Exception {
		Assert.hasLength(nome, "nome é obrigatorio");

		this.status = EnumStatus.INATIVO;
		this.nome = nome;
		this.endereco = endereco;
		this.cnpj = cnpj;
		this.criadoEm = LocalDateTime.now();
		valida();
	}

	public Tenant(TenantRequestDto dto, String endereco) throws Exception {
		this(dto.getNome(), endereco, dto.getCpfCnpj());
	}

	public Tenant(Long tenantId) {
		this.id = tenantId;
	}

	public void ativaTenant() {
		this.status = EnumStatus.ATIVO;
		this.atualizadoEm = LocalDateTime.now();
	}

	public void valida() {
		AssertionConcern.ValideIsNotEmptyOrBlank(nome, String.format(NULO_OU_VAZIO, "nome"));
		AssertionConcern.ValideIsNotEmptyOrBlank(cnpj, String.format(NULO_OU_VAZIO, "CNPJ"));
	}

}
