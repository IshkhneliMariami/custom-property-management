package ge.redmed.custompropertymanagement.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "properties")
@SequenceGenerator(name = "propertiesSeq", sequenceName = "properties_id_seq", allocationSize = 1)
public class Property {
	@Id
	@GeneratedValue(generator = "propertiesSeq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "active")
	private Boolean active;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "users_properties", joinColumns = {
			@JoinColumn(name = "property_id", referencedColumnName = "id")
	}, inverseJoinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "id")
	})
	private List<User> propertiesUsers;
}
