package ge.redmed.custompropertymanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {
	@Value("${spring.servlet.multipart.location}")
	String pathName;

	@Override
	public void uploadFile(MultipartFile file) throws IOException {
		file.transferTo(new File("pathName"));
	}
}
