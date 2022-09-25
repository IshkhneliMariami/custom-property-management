package ge.redmed.custompropertymanagement.repository;

import ge.redmed.custompropertymanagement.model.PropertyDeletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyDeletionRepository extends JpaRepository<PropertyDeletion, Long> {
	List<PropertyDeletion> findByPropertyIdAndConfirmIsTrue(long propertyId);
}
