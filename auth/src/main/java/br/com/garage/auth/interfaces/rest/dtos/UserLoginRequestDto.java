package br.com.garage.auth.interfaces.rest.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserLoginRequestDto {

    @NotBlank
    public String email;

    @NotBlank
    @Size(min = 8, max = 30)
    public String password;


}
