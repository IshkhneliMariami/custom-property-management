package ge.redmed.custompropertymanagement.repository;

import ge.redmed.custompropertymanagement.model.Property;
import ge.redmed.custompropertymanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUserNameAndActiveTrue(String userName);

	@Lock(LockModeType.PESSIMISTIC_READ)
	@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
	@Query("""    
			   select propertyId
			   from UsersProperty
			   where userId = :userId
			""")
	List<Long> getUsersProperties(long userId);
}
