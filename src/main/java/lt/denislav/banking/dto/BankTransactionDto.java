package lt.denislav.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lt.denislav.banking.model.TransactionType;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankTransactionDto {

	@NotNull
	private BigDecimal amount;

	@NotNull
	private TransactionType transactionType;

	@Getter
	private Instant created;

	public BankTransactionDto(@NotNull BigDecimal amount,
			@NotNull TransactionType transactionType) {
		this.amount = amount;
		this.transactionType = transactionType;
	}
}
