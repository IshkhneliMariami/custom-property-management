package ge.redmed.custompropertymanagement.service;

import ge.redmed.custompropertymanagement.model.Property;
import ge.redmed.custompropertymanagement.model.Role;
import ge.redmed.custompropertymanagement.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public interface PropertyService {
	List<Property> getAllProperty();

	Property saveProperty(Property property, MultipartFile file) throws IOException;

	void addPropertyToUser(String userName, long id);

	Property getProperty(long id);

	Page<Property> search(String name, Pageable pageable);

	void delete(long id, MultipartFile file) throws IOException;

	void confirmDeletion(long id, boolean confirm);

	void export(HttpServletResponse response) throws IOException;
}
