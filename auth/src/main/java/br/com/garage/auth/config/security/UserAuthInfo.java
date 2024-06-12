package br.com.garage.auth.config.security;


import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class UserAuthInfo implements Serializable {

    private final UUID usuarioId;
    private final UUID tenantId;
    private final String email;
    private final String roles;

    public UserAuthInfo(String usuarioId, String tenantId, String email, String roles) {
        this.usuarioId = usuarioId != null ? UUID.fromString(usuarioId) : null;
        this.tenantId = tenantId != null ? UUID.fromString(tenantId) : null;
        this.email = email;
        this.roles = roles;
    }

}
