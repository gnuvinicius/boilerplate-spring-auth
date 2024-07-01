package br.com.garage.auth.interfaces.rest.dtos;

import lombok.Data;

@Data
public class RequestRefreshPasswordDto {
    private String email;
    private String tokenRefreshPassword;
    private String newPassword;
}