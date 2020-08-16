package lt.denislav.banking.api;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lt.denislav.banking.exception.NotSufficientBalanceException;
import lt.denislav.banking.exception.UserExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@ResponseBody
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	OperationErrorResponse badRequest(NotSufficientBalanceException e) {
		return new OperationErrorResponse(e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	OperationErrorResponse badRequest(UserExistsException e) {
		return new OperationErrorResponse(e.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public OperationErrorResponse handleJsonValidationExceptions(HttpMessageNotReadableException ex) {
		return new OperationErrorResponse("Invalid Json body");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public OperationErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
		return new OperationErrorResponse(buildValidationErrors(ex));
	}

	private Map<String, String> buildValidationErrors(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

	@AllArgsConstructor
	public static class OperationErrorResponse {
		public final Object message;
	}
}
