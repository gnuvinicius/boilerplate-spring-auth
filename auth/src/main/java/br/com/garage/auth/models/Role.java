package br.com.garage.auth.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@Setter
@Table(name = "tb_roles")
public class Role implements GrantedAuthority {

	@Id
	@Column(name = "role_id")
	private Long id;

	@Column(nullable = false, unique = true)
	private String roleName;

	@Override
	public String getAuthority() {
		return this.roleName;
	}

}
