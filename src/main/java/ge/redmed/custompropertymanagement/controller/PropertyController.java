package ge.redmed.custompropertymanagement.controller;

import ge.redmed.custompropertymanagement.model.Property;
import ge.redmed.custompropertymanagement.model.excelGenerator.DataGenerator;
import ge.redmed.custompropertymanagement.model.excelGenerator.ExcelGenerator;
import ge.redmed.custompropertymanagement.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("properties")
@RequiredArgsConstructor
public class PropertyController {
	private final PropertyService propertyService;

	@GetMapping
	public Page<Property> search(@RequestParam(required = false) String name,
								 @RequestParam(value = "limit", required = false, defaultValue = "25") int size,
								 @RequestParam(value = "page", required = false, defaultValue = "1") int page,
								 @RequestParam(value = "start", required = false, defaultValue = "0") int start) {
		return propertyService.search(name, PageRequest.of(page - 1, size, Sort.Direction.DESC, "id"));

	}

	@PostMapping("/save")
	public Property saveProperty(@RequestBody Property property,
								 @RequestParam MultipartFile file) throws IOException {
		return propertyService.saveProperty(property, file);
	}

	@PostMapping("{id}/add-to-user")
	public void addPropertyToUser(@PathVariable long id,
								  @RequestParam String userName) {
		propertyService.addPropertyToUser(userName, id);
	}

	@PutMapping("{id}")
	public Property updateProperty(@PathVariable long id,
								   @RequestBody Property property,
								   @RequestParam MultipartFile file) throws IOException {
		property.setId(id);
		return propertyService.saveProperty(property, file);
	}

	@DeleteMapping("{id}")
	public void deleteProperty(@PathVariable long id,
							   @RequestParam MultipartFile file) throws IOException {
		propertyService.delete(id, file);
	}

	@PostMapping("{id}/confirm-deletion")
	public void confirmPropertyDeletion(@PathVariable long id,
										@RequestParam boolean confirm) {
		propertyService.confirmDeletion(id, confirm);
	}

	@GetMapping("/excel")
	public void export(HttpServletResponse response) throws IOException {
		propertyService.export(response);
	}
}
