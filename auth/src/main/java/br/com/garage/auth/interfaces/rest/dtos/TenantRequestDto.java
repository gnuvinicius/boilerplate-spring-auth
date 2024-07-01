package br.com.garage.auth.interfaces.rest.dtos;

import br.com.garage.auth.models.Tenant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @NotNull
    private EnderecoDto endereco;

    @NotEmpty
    @NotNull
    @NotBlank
    private String cnpj;

    @NotNull
    private UsuarioRequestDto admin;

    public Tenant toModel() throws Exception {

        return new Tenant(this.nome, enderecoToString(), this.cnpj);
    }

    private String enderecoToString() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String endereco = objectMapper
                .writeValueAsString(objectMapper.writeValueAsString(this.endereco));

        endereco = endereco.replaceAll("\\\\", "");
        endereco = endereco.replace("\"{", "{");
        endereco = endereco.replace("}\"", "}");
        return endereco;
    }
}
