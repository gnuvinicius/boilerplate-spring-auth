package br.com.garage.auth.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResponseErrorBody {

    public LocalDateTime timestamp;
    public String message;
    public int status;
}
