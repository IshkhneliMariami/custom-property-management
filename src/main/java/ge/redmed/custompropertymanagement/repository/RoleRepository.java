package ge.redmed.custompropertymanagement.repository;

import ge.redmed.custompropertymanagement.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);
}
