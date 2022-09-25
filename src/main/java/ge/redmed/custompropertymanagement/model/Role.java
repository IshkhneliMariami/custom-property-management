package ge.redmed.custompropertymanagement.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "roles")
@SequenceGenerator(name = "rolesSeq", sequenceName = "roles_id_seq", allocationSize = 1)
public class Role {
	@Id
	@GeneratedValue(generator = "rolesSeq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;
}
