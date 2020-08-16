package lt.denislav.banking.exception;

public class UserExistsException extends RuntimeException {
	public UserExistsException() {
		super("User with such e-mail already exists");
	}
}