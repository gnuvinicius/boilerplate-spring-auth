package br.com.garage.auth.interfaces.rest.dtos;

import br.com.garage.auth.models.Tenant;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class TenantRequestDto {

	private String nome;
	private String endereco;
	private String cnpj;
	private UsuarioRequestDto admin;
	
	public Tenant toModel() throws Exception {
		return new Tenant(this.nome, this.endereco, this.cnpj);
	}
}
