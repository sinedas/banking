package lt.denislav.banking.repository;

import lt.denislav.banking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AccountRepository extends JpaRepository<Account, Long> {

	Account findByUserId(Long id);

	/**
	 * Only in unit tests
	 */
	@Modifying
	@Query("UPDATE Account a SET a.balance = 0")
	@Transactional
	void resetAllAccounts();
}
