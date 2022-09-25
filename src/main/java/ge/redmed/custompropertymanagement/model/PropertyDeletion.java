package ge.redmed.custompropertymanagement.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "property_deletion")
public class PropertyDeletion {
	@Id
	@GeneratedValue(generator = "propertyDeletionSeq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "property_id")
	private Long propertyId;

	@Column(name = "confirm")
	private Boolean confirm;
}
