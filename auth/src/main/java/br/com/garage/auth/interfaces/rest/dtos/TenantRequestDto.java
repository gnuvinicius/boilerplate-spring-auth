package br.com.garage.auth.interfaces.rest.dtos;

import br.com.garage.auth.models.Tenant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class TenantRequestDto {

    @NotBlank
    @NotNull
    @NotEmpty
    private String nome;

    private EnderecoDto endereco;

    @NotEmpty
    @NotNull
    @NotBlank
    @Size(min = 11, max = 14, message = "o CNPJ está invalido")
    private String cpfCnpj;

    @NotNull
    private UsuarioRequestDto admin;

    public Tenant toModel() throws Exception {

        return new Tenant(this.nome, enderecoToString(), this.cpfCnpj);
    }

    private String enderecoToString() throws JsonProcessingException {
        if (this.endereco == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String endereco = objectMapper
                .writeValueAsString(objectMapper.writeValueAsString(this.endereco));

        endereco = endereco.replaceAll("\\\\", "");
        endereco = endereco.replace("\"{", "{");
        endereco = endereco.replace("}\"", "}");
        return endereco;
    }
}
