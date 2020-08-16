package lt.denislav.banking.api;

import lombok.RequiredArgsConstructor;
import lt.denislav.banking.dto.RegisterUserDto;
import lt.denislav.banking.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Validated
@RequestMapping(value = "/users", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@PostMapping("register")
	public void register(@Validated @RequestBody RegisterUserDto userDto) {
		userService.register(userDto);
	}

}
