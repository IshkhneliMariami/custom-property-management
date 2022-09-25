package ge.redmed.custompropertymanagement.service;

import ge.redmed.custompropertymanagement.configuration.WebSecurityConfig;
import ge.redmed.custompropertymanagement.model.*;
import ge.redmed.custompropertymanagement.model.excelGenerator.DataGenerator;
import ge.redmed.custompropertymanagement.model.excelGenerator.ExcelGenerator;
import ge.redmed.custompropertymanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {
	private final PropertyRepository propertyRepository;
	private final UserRepository userRepository;
	private final UserPropertyRepository userPropertyRepository;
	private final PropertyDeletionRepository propertyDeletionRepository;
	private final FileUploadService fileUploadService;

	@Override
	public List<Property> getAllProperty() {
		return propertyRepository.findAll();
	}

	@Override
	public Property saveProperty(Property property, MultipartFile file) throws IOException {
		fileUploadService.uploadFile(file);
		return propertyRepository.save(property);
	}

	@Override
	@CacheEvict(value = "PROPERTY", allEntries = true)
	public void addPropertyToUser(String userName, long id) {
		User user = userRepository.findByUserNameAndActiveTrue(userName);
		UsersProperty usersProperty = new UsersProperty(user.getId(), id);
		userPropertyRepository.save(usersProperty);
	}

	@Cacheable("PROPERTY")
	public Property getProperty(long id) {
		var propertyOpt = propertyRepository.findById(id);
		if (propertyOpt.isEmpty()) {
			throw new RuntimeException("Property with not found");
		}
		return propertyOpt.get();
	}

	@Override
	public Page<Property> search(String name,
								 Pageable pageable) {
		return propertyRepository.findAll((root, query, cb) -> {
			Predicate predicate = cb.conjunction();
			if (StringUtils.hasText(name)) {
				predicate = cb.and(predicate, cb.like(root.get(Property.class.getName()), "%" + name + "%"));
			}
			return predicate;
		}, pageable);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = "PROPERTY", allEntries = true)
	public void delete(long id, MultipartFile file) throws IOException {
		Property property = getProperty(id);
		var propertyUsers = property.getPropertiesUsers();
		if (propertyUsers != null) {
			var confirmedDeletion = propertyDeletionRepository.findByPropertyIdAndConfirmIsTrue(id);
			if (confirmedDeletion != null) {
				if (confirmedDeletion.size() != propertyUsers.size() - 1) {
					throw new RuntimeException("Property has more than one owner, it can't be deleted without confirmation");
				}
			}
		}
		property.setActive(false);
		saveProperty(property, file);
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void confirmDeletion(long id, boolean confirm) {
		var currentUserId = WebSecurityConfig.getCurrentUserId();
		PropertyDeletion propertyDeletion = new PropertyDeletion();
		propertyDeletion.setPropertyId(id);
		propertyDeletion.setUserId(currentUserId);
		propertyDeletion.setConfirm(confirm);
		propertyDeletionRepository.save(propertyDeletion);
	}

	public void export(HttpServletResponse response) throws IOException {
		var properties = propertyRepository.findAll();
		List<Object[]> data = new ArrayList<>();
		properties.forEach(property -> {
			Object[] row = new Object[2];
			row[0] = property.getName();
			row[1] = property.getActive();
			data.add(row);
		});

		String[] headers = new String[]{
				"დასახელება",
				"სტატუსი",
		};

		final String fileName = "properties.xlsx";

		DataGenerator dataGenerator = new ExcelGenerator();
		Workbook workBook = dataGenerator.generate(headers, data);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		workBook.write(response.getOutputStream());
	}
}
