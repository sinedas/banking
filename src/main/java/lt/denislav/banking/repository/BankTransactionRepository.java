package lt.denislav.banking.repository;

import lt.denislav.banking.model.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {

	List<BankTransaction> findAllByAccountUserId(Long userId);
}
