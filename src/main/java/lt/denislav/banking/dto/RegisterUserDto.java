package lt.denislav.banking.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RegisterUserDto {

	@Email
	@NotNull
	private String email;

	@NotEmpty
	private String password;
}
