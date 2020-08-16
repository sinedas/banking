package lt.denislav.banking.api;

import lombok.RequiredArgsConstructor;
import lt.denislav.banking.dto.AccountStatementDto;
import lt.denislav.banking.dto.AuthUserDetails;
import lt.denislav.banking.dto.BankTransactionDto;
import lt.denislav.banking.service.AccountService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;



@RestController
@RequestMapping(value = "/account", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountsService;

	@GetMapping("/statement")
	public AccountStatementDto statement() {
		AuthUserDetails authUserDetails = (AuthUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return accountsService.statement(authUserDetails.getUserId());
	}

	@PostMapping("/transaction")
	public void transaction(@Validated @RequestBody BankTransactionDto bankTransactionDto) {
		AuthUserDetails authUserDetails = (AuthUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		accountsService.transaction(authUserDetails.getUserId(), bankTransactionDto);
	}
}
