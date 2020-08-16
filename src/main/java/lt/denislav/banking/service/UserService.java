package lt.denislav.banking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lt.denislav.banking.dto.RegisterUserDto;
import lt.denislav.banking.exception.UserExistsException;
import lt.denislav.banking.model.Account;
import lt.denislav.banking.model.User;
import lt.denislav.banking.repository.AccountRepository;
import lt.denislav.banking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

	private final UserRepository userRepository;
	private final AccountRepository accountRepository;

	@Transactional
	public User register(RegisterUserDto userDto) {
		log.debug("Register user: {}", userDto.getEmail());
		Optional<User> userPresent = userRepository.findByEmail(userDto.getEmail());
		if (userPresent.isPresent()) {
			throw new UserExistsException();
		}

		User user = new User();
		user.setPassword(userDto.getPassword());
		user.setEmail(userDto.getEmail());
		userRepository.save(user);

		Account account = new Account();
		account.setUser(user);
		accountRepository.save(account);

		return userRepository.save(user);
	}
}
