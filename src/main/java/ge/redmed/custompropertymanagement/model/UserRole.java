package ge.redmed.custompropertymanagement.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user_roles")
@NoArgsConstructor
@SequenceGenerator(name = "userRolesSeq", sequenceName = "user_roles_id_seq", allocationSize = 1)

public class UserRole {

	@Id
	@GeneratedValue(generator = "userRolesSeq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "role_id")
	private Long roleId;

	public UserRole(Long userId, Long roleId) {
		this.userId = userId;
		this.roleId = roleId;
	}


}
