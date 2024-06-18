package br.com.garage.auth.interfaces.rest.dtos;

import br.com.garage.auth.models.Tenant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class TenantRequestDto {

    @NotBlank
    @NotNull
    @NotEmpty
    private String nome;
    private String endereco;

    @NotEmpty
    @NotNull
    @NotBlank
    private String cnpj;

    @NotNull
    private UsuarioRequestDto admin;

    public Tenant toModel() throws Exception {
        return new Tenant(this.nome, this.endereco, this.cnpj);
    }
}
