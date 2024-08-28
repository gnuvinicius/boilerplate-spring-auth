package br.com.garage.auth.interfaces.rest.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequestDto {

    private String nome;

    @NotNull
    @NotEmpty
    @NotBlank
    private String email;

    @Size(min = 8, max = 30)
    private String password;

    private String confirmPassword;

    private boolean primeiroAcesso = false;
    private boolean sendWelcomeEmail = false;
    private boolean isAdmin = false;
}
