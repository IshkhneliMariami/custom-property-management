package ge.redmed.custompropertymanagement.repository;

import ge.redmed.custompropertymanagement.model.UsersProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPropertyRepository extends JpaRepository<UsersProperty, Long> {

}
