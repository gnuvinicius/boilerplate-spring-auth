package br.com.garage.auth.interfaces.rest.dtos;

import java.util.UUID;

public class UserDto {

  private UUID id;
  private String login;
  private String token;

  public UserDto(UUID id, String login, String token) {
    this.id = id;
    this.login = login;
    this.token = token;
  }

  public UUID getId() {
    return this.id;
  }

  public String getLogin() {
    return this.login;
  }

  public String getToken() {
    return this.token;
  }

}
