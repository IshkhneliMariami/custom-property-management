package ge.redmed.custompropertymanagement.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@SequenceGenerator(name = "usersSeq", sequenceName = "users_id_seq", allocationSize = 1)

public class User {

//	public enum Roles {
//		ROLE_USER, ROLE_SUPPORT, ROLE_PROPERTY_ADMIN, ROLE_ADMIN
//	}

	@Id
	@GeneratedValue(generator = "usersSeq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "password")
	private String password;

	@Column(name = "active")
	private Boolean active;


//	@Enumerated(EnumType.ORDINAL)
//	@Column(name = "role")
//	private Roles role;

	@ManyToMany
	@JoinTable(name = "user_roles", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "id")
	}, inverseJoinColumns = {
			@JoinColumn(name = "role_id", referencedColumnName = "id")
	})
	private List<Role> userRoles;

}
