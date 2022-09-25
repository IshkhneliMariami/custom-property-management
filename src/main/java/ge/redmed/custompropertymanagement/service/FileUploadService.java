package ge.redmed.custompropertymanagement.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileUploadService {

	void uploadFile(MultipartFile file) throws IOException;
}
