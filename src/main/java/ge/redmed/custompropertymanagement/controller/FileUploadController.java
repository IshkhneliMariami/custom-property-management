package ge.redmed.custompropertymanagement.controller;

import ge.redmed.custompropertymanagement.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("files")
public class FileUploadController {

	private final FileUploadService fileUploadService;

	@PostMapping
	public void uploadFile(@RequestParam MultipartFile file) throws IOException {
		fileUploadService.uploadFile(file);
	}
}
