package br.com.garage.auth.config;

import br.com.garage.commons.exceptions.BusinessException;
import br.com.garage.commons.exceptions.NotFoundException;
import br.com.garage.commons.exceptions.UnauthorizedException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@RestController
@Validated
@RestControllerAdvice
@Log4j2
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

	private static final String STATUS = "status";
	private static final String MESSAGE = "message";
	private static final String TIMESTAMP = "timestamp";

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ApiResponse(responseCode = "400", description = "bad request",
			content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = ResponseErrorBody.class)) })
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ResponseErrorBody> handleException(DataIntegrityViolationException ex) {
		log.error("{}", ex.getMessage());
		var body = new ResponseErrorBody(LocalDateTime.now(), ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ApiResponse(responseCode = "400", description = "bad request",
			content = { @Content(mediaType = "application/json",
			schema = @Schema(implementation = ResponseErrorBody.class)) })
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ResponseErrorBody> handleBussinessException(BusinessException ex) {
		log.error("{}", ex.getMessage());
		var body = new ResponseErrorBody(LocalDateTime.now(), ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ApiResponse(responseCode = "401", description = "unauthorized",
			content = { @Content(mediaType = "application/json",
			schema = @Schema(implementation = ResponseErrorBody.class)) })
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ResponseErrorBody> handleUnauthorizedException(UnauthorizedException ex) {
		log.error("{}", ex.getMessage());
		var body = new ResponseErrorBody(LocalDateTime.now(), ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ApiResponse(content = { @Content(mediaType = "application/json",
			schema = @Schema(implementation = ResponseErrorBody.class)) })
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ResponseErrorBody> handleNotFoundException(NotFoundException ex) {
		log.error("{}", ex.getMessage());
		var body = new ResponseErrorBody(LocalDateTime.now(), ex.getMessage(), HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ApiResponse(content = { @Content(mediaType = "application/json",
			schema = @Schema(implementation = ResponseErrorBody.class)) })
	@ExceptionHandler(URISyntaxException.class)
	public ResponseEntity<ResponseErrorBody> handleURISyntaxException(URISyntaxException ex) {
		log.error("{}", ex.getMessage());
		var body = new ResponseErrorBody(LocalDateTime.now(), ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ApiResponse(content = { @Content(mediaType = "application/json",
			schema = @Schema(implementation = ResponseErrorBody.class)) })
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ResponseErrorBody> handleConstraintViolationException(ConstraintViolationException ex) {
		log.error("{}", ex.getMessage());
		var body = new ResponseErrorBody(LocalDateTime.now(), ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ApiResponse(content = { @Content(mediaType = "application/json",
			schema = @Schema(implementation = ResponseErrorBody.class)) })
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ResponseErrorBody> handleConstraintViolationException(IllegalArgumentException ex) {
		log.error("{}", ex.getMessage());
		var body = new ResponseErrorBody(LocalDateTime.now(), ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		List<String> errorList = new ArrayList<>();

		log.error("{}", ex.getMessage());

		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			StringBuilder errors = new StringBuilder();
			errors.append(fieldName);
			errors.append(" ");
			errors.append(errorMessage);
			errorList.add(errors.toString());
		});
		body.put(TIMESTAMP, LocalDateTime.now());
		body.put(MESSAGE, errorList);
		body.put(STATUS, HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
}
