package lt.denislav.banking.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import lt.denislav.banking.BankingApplication;
import lt.denislav.banking.dto.AccountStatementDto;
import lt.denislav.banking.model.BankTransaction;
import lt.denislav.banking.model.TransactionType;
import lt.denislav.banking.repository.AccountRepository;
import lt.denislav.banking.repository.BankTransactionRepository;
import lt.denislav.banking.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BankingApplication.class)
@AutoConfigureMockMvc
@Log4j2
class AccountControllerTest {

	HttpHeaders securityAndLocaleHeaders;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	UserService userService;

	@Autowired
	BankTransactionRepository bankTransactionRepository;

	@Autowired
	AccountRepository accountRepository;

	ObjectMapper objectMapper;

	@BeforeEach
	void setUp() throws Exception {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		authorizeWithCredentials("test@banking", "password");
	}

	@AfterEach
	void tearDown() {
		bankTransactionRepository.deleteAll();
		accountRepository.resetAllAccounts();
	}

	@Test
	void accountStatementOfNewUser() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/account/statement")
				.headers(securityAndLocaleHeaders)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		AccountStatementDto accountStatementDto = objectMapper
				.readValue(resultActions.andReturn().getResponse().getContentAsString(), AccountStatementDto.class);

		assertThat(accountStatementDto.getAmount(), equalTo(new BigDecimal("0.00")));
		assertThat(accountStatementDto.getTransactions().size(), equalTo(0));
	}

	@Test
	void depositAndWithDrawal() throws Exception {
		BankTransaction depositTransaction = new BankTransaction();
		depositTransaction.setTransactionType(TransactionType.DEPOSIT);
		depositTransaction.setAmount(new BigDecimal("12.02"));
		mockMvc.perform(post("/account/transaction")
				.headers(securityAndLocaleHeaders)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(depositTransaction)))
				.andExpect(status().isOk());

		ResultActions resultActions = mockMvc.perform(get("/account/statement")
				.headers(securityAndLocaleHeaders)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		AccountStatementDto accountStatementDto = objectMapper
				.readValue(resultActions.andReturn().getResponse().getContentAsString(), AccountStatementDto.class);

		assertThat(accountStatementDto.getAmount(), equalTo(new BigDecimal("12.02")));
		assertThat(accountStatementDto.getTransactions().size(), equalTo(1));
		assertThat(accountStatementDto.getTransactions().get(0).getTransactionType(), equalTo(TransactionType.DEPOSIT));
		assertThat(accountStatementDto.getTransactions().get(0).getAmount(), equalTo(new BigDecimal("12.02")));

		BankTransaction withDrawalTransaction = new BankTransaction();
		withDrawalTransaction.setTransactionType(TransactionType.WITHDRAWAL);
		withDrawalTransaction.setAmount(new BigDecimal("12.02"));
		mockMvc.perform(post("/account/transaction")
				.headers(securityAndLocaleHeaders)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(withDrawalTransaction)))
				.andExpect(status().isOk());

		resultActions = mockMvc.perform(get("/account/statement")
				.headers(securityAndLocaleHeaders)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		accountStatementDto = objectMapper
				.readValue(resultActions.andReturn().getResponse().getContentAsString(), AccountStatementDto.class);

		assertThat(accountStatementDto.getAmount(), equalTo(new BigDecimal("0.00")));
		assertThat(accountStatementDto.getTransactions().size(), equalTo(2));
		assertThat(accountStatementDto.getTransactions().get(0).getTransactionType(), equalTo(TransactionType.DEPOSIT));
		assertThat(accountStatementDto.getTransactions().get(0).getAmount(), equalTo(new BigDecimal("12.02")));
		assertThat(accountStatementDto.getTransactions().get(1).getTransactionType(), equalTo(TransactionType.WITHDRAWAL));
		assertThat(accountStatementDto.getTransactions().get(1).getAmount(), equalTo(new BigDecimal("12.02")));
	}

	@Test
	void notSufficientBalanceWithDrawal() throws Exception {
		BankTransaction withDrawalTransaction = new BankTransaction();
		withDrawalTransaction.setTransactionType(TransactionType.WITHDRAWAL);
		withDrawalTransaction.setAmount(new BigDecimal("0.01"));
		mockMvc.perform(post("/account/transaction")
				.headers(securityAndLocaleHeaders)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(withDrawalTransaction)))
				.andExpect(status().isNotAcceptable())
				.andExpect(jsonPath("$.message", is("Not sufficient balance for withdrawal")));
		;
	}

	public void authorizeWithCredentials(String username, String password) throws Exception {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "password");
		params.add("username", username);
		params.add("password", password);

		byte[] clientSecret = ("banking:thisissecret").getBytes(StandardCharsets.UTF_8);
		ResultActions result
				= mockMvc.perform(post("/oauth/token")
				.params(params)
				.header(HttpHeaders.AUTHORIZATION, "Basic " + new String(Base64.getEncoder().encode(clientSecret)))
				.accept("application/json;charset=UTF-8"))
				.andExpect(status().isOk());

		String resultString = result.andReturn().getResponse().getContentAsString();

		JacksonJsonParser jsonParser = new JacksonJsonParser();
		String accessToken = jsonParser.parseMap(resultString).get("access_token").toString();

		log.debug("access_token: {}", accessToken);
		securityAndLocaleHeaders = new HttpHeaders();
		securityAndLocaleHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer" + accessToken);
	}
}
