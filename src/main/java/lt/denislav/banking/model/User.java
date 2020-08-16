package lt.denislav.banking.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@Table(name = "users")
@ToString(exclude = {"password"})
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

	/**
	 * E-mail is used as username.
	 */
	@Email
	private String email;

	@NotEmpty
	private String password;
}
