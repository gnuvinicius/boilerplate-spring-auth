package br.com.garage.auth.models;

import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tb_role")
public class Role implements GrantedAuthority {

	@Id
	@Column(name = "role_id")
	private UUID id;

	@Column(nullable = false, unique = true)
	private String roleName;

	@Override
	public String getAuthority() {
		return this.roleName;
	}

}
