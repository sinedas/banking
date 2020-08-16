package lt.denislav.banking.exception;

public class NotSufficientBalanceException extends RuntimeException {
	public NotSufficientBalanceException() {
		super("Not sufficient balance for withdrawal");
	}
}
