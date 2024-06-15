package br.com.garage.commons.utils.userInfo;


import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class UserAuthInfo implements Serializable {

    private final Long usuarioId;
    private final Long tenantId;
    private final String email;
    private final String roles;

    public UserAuthInfo(Long usuarioId, Long tenantId, String email, String roles) {
        this.usuarioId = usuarioId;
        this.tenantId = tenantId;
        this.email = email;
        this.roles = roles;
    }

}
