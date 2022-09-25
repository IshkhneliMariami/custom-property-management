package ge.redmed.custompropertymanagement.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "users_properties")
@NoArgsConstructor
@SequenceGenerator(name = "usersPropertiesSeq", sequenceName = "users_properties_id_seq", allocationSize = 1)
public class UsersProperty {
	@Id
	@GeneratedValue(generator = "usersPropertiesSeq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "property_id")
	private Long propertyId;

	public UsersProperty(long userId, long propertyId) {
		this.userId = userId;
		this.propertyId = propertyId;
	}

}
