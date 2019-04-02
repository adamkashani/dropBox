package core.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.javaBeans.FileEntity;
import core.service.FileService;

@RestController
@RequestMapping("/file")
public class ApiFile {

	@Autowired
	private FileService fileController;
	
	public ApiFile() {};
	
	
	@PostMapping
	public void createFile (@RequestBody FileEntity fileText) {	
		fileController.create(fileText);	
	}
	
	
	

	
}
