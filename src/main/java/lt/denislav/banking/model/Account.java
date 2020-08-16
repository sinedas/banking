package lt.denislav.banking.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "accounts")
@Entity
@EqualsAndHashCode(callSuper = true)
public class Account extends BaseEntity {

	private BigDecimal balance = BigDecimal.ZERO;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

}
