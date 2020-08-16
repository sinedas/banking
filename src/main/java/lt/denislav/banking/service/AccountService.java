package lt.denislav.banking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lt.denislav.banking.dto.AccountStatementDto;
import lt.denislav.banking.dto.BankTransactionDto;
import lt.denislav.banking.exception.NotSufficientBalanceException;
import lt.denislav.banking.model.Account;
import lt.denislav.banking.model.BankTransaction;
import lt.denislav.banking.model.TransactionType;
import lt.denislav.banking.repository.AccountRepository;
import lt.denislav.banking.repository.BankTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class AccountService {

	private final AccountRepository accountRepository;

	private final BankTransactionRepository bankTransactionRepository;

	public Account findUserAccount(Long userId) {
		return accountRepository.findByUserId(userId);
	}

	@Transactional
	public void transaction(Long userId, BankTransactionDto bankTransactionDto) {
		log.info("transaction, user {}, amount: {}, type: {} ", userId, bankTransactionDto.getAmount(), bankTransactionDto.getTransactionType());

		Account account = accountRepository.findByUserId(userId);

		if (TransactionType.DEPOSIT == bankTransactionDto.getTransactionType()) {
			account.setBalance(account.getBalance().add(bankTransactionDto.getAmount()));
		} else {
			BigDecimal balance = account.getBalance().subtract(bankTransactionDto.getAmount());
			if (balance.compareTo(BigDecimal.ZERO) < 0) {
				throw new NotSufficientBalanceException();
			}

			account.setBalance(balance);
		}

		accountRepository.save(account);

		BankTransaction bankTransaction = new BankTransaction();
		bankTransaction.setAccount(account);
		bankTransaction.setAmount(bankTransactionDto.getAmount());
		bankTransaction.setTransactionType(bankTransactionDto.getTransactionType());
		bankTransaction.setCreated(Instant.now());
		bankTransactionRepository.save(bankTransaction);
	}

	public AccountStatementDto statement(Long userId) {
		log.info("statement, user {}", userId);

		List<BankTransaction> transactions = bankTransactionRepository.findAllByAccountUserId(userId);
		List<BankTransactionDto> transactionDtos = transactions.stream()
				.map(this::createBankTransactionDto)
				.collect(Collectors.toList());
		Account account = accountRepository.findByUserId(userId);

		return new AccountStatementDto(account.getBalance(), transactionDtos);
	}

	private BankTransactionDto createBankTransactionDto(BankTransaction transaction) {
		BankTransactionDto bankTransactionDto = new BankTransactionDto();
		bankTransactionDto.setAmount(transaction.getAmount());
		bankTransactionDto.setTransactionType(transaction.getTransactionType());
		bankTransactionDto.setCreated(transaction.getCreated());
		return bankTransactionDto;
	}

}
