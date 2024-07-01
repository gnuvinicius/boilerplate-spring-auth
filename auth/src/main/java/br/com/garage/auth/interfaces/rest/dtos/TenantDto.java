package br.com.garage.auth.interfaces.rest.dtos;

import br.com.garage.commons.enums.EnumStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TenantDto {
    private String id;
    private String nome;
    private String endereco;
    private String cnpj;
    private EnumStatus status;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}