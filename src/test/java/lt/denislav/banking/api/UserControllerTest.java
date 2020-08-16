package lt.denislav.banking.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.denislav.banking.BankingApplication;
import lt.denislav.banking.dto.RegisterUserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BankingApplication.class)
@AutoConfigureMockMvc
class UserControllerTest {

	@Autowired
	MockMvc mockMvc;

	ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void registerUserFailedBecauseOfNotValidForm() throws Exception {
		Map<String, String> expectedMessage = new LinkedHashMap();
		expectedMessage.put("password", "must not be empty");
		expectedMessage.put("email", "must not be null");

		mockMvc.perform(post("/users/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new RegisterUserDto())))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is(expectedMessage)));
	}

	@Test
	void registerUserOnceAndSecondAtemptFailed() throws Exception {
		RegisterUserDto registerUserDto = new RegisterUserDto();
		registerUserDto.setEmail("first@email.com");
		registerUserDto.setPassword("password");

		mockMvc.perform(post("/users/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerUserDto)))
				.andExpect(status().isOk());

		mockMvc.perform(post("/users/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerUserDto)))
				.andExpect(status().isNotAcceptable())
				.andExpect(jsonPath("$.message", is("User with such e-mail already exists")));
	}

	@Test
	void registerUserAndLoginSuccesfull() throws Exception {
		RegisterUserDto registerUserDto = new RegisterUserDto();
		registerUserDto.setEmail("second@email.com");
		registerUserDto.setPassword("password");

		mockMvc.perform(post("/users/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerUserDto)))
				.andExpect(status().isOk());

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "password");
		params.add("username", "second@email.com");
		params.add("password", "password");

		byte[] clientSecret = ("banking:thisissecret").getBytes(StandardCharsets.UTF_8);
		mockMvc.perform(post("/oauth/token")
				.params(params)
				.header(HttpHeaders.AUTHORIZATION, "Basic " + new String(Base64.getEncoder().encode(clientSecret)))
				.accept("application/json;charset=UTF-8"))
				.andExpect(status().isOk());
	}

}
