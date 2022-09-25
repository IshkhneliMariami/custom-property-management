package ge.redmed.custompropertymanagement.service;

import ge.redmed.custompropertymanagement.configuration.WebSecurityConfig;
import ge.redmed.custompropertymanagement.model.Property;
import ge.redmed.custompropertymanagement.model.Role;
import ge.redmed.custompropertymanagement.model.User;
import ge.redmed.custompropertymanagement.model.UserRole;
import ge.redmed.custompropertymanagement.model.excelGenerator.DataGenerator;
import ge.redmed.custompropertymanagement.model.excelGenerator.ExcelGenerator;
import ge.redmed.custompropertymanagement.repository.RoleRepository;
import ge.redmed.custompropertymanagement.repository.UserRepository;
import ge.redmed.custompropertymanagement.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserRoleRepository userRoleRepository;
	private final PropertyService propertyService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUserNameAndActiveTrue(username);
		if (user == null) {
			throw new UsernameNotFoundException("user with user name not found");
		}
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		user.getUserRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), authorities);
	}

	@Override
	public List<User> getAllUser() {
		return userRepository.findAll();
	}

	@Override
	public User saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public Role saveRole(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public void addRoleToUser(long id, String roleName) {
		Role role = roleRepository.findByName(roleName);
		UserRole userRole = new UserRole(id, role.getId());
		userRoleRepository.save(userRole);
	}

	@Override
	public User getUser(String userName) {
		return userRepository.findByUserNameAndActiveTrue(userName);
	}

	@Override
	public void exportUsersPropertiesPriceSum(HttpServletResponse response) throws IOException {
		var user = WebSecurityConfig.getCurrentUser();
		var priceSum = getUsersPropertiesPriceSum(user.getId());
		List<Object[]> data = new ArrayList<>();
		Object[] row = new Object[2];
		row[0] = priceSum;
		row[1] = user.getUserName();
		data.add(row);

		String[] headers = new String[]{
				"ჯამი",
				"მომხმარებლის სახელი",
		};

		final String fileName = "usersProperties.xlsx";

		DataGenerator dataGenerator = new ExcelGenerator();
		Workbook workBook = dataGenerator.generate(headers, data);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		workBook.write(response.getOutputStream());
	}

	public synchronized BigDecimal getUsersPropertiesPriceSum(long userId) {
		List<Long> propertiesIds = userRepository.getUsersProperties(userId);
		List<Property> usersProperties = new ArrayList<>();
		propertiesIds.forEach(id -> {
			usersProperties.add(propertyService.getProperty(id));
		});
		return usersProperties
				.stream()
				.map(Property::getPrice)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

}
