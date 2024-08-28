package br.com.garage.auth.interfaces.rest.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenDto {

	public @JsonProperty(value = "access_token") String accessToken;
	public String type;
	public String tenantName;

	public TokenDto(String accessToken, String type, String tenantName) {
		this.accessToken = accessToken;
		this.type = type;
		this.tenantName = tenantName;
	}
}
