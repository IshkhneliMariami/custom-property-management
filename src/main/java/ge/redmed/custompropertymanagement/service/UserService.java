package ge.redmed.custompropertymanagement.service;

import ge.redmed.custompropertymanagement.model.Role;
import ge.redmed.custompropertymanagement.model.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public interface UserService {
	List<User> getAllUser();

	User saveUser(User user);

	Role saveRole(Role role);

	void addRoleToUser(long id, String roleName);

	User getUser(String userName);

	void exportUsersPropertiesPriceSum(HttpServletResponse response) throws IOException;
}
