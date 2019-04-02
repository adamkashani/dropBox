package core.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import core.javaBeans.BeanBinaryFile;
import core.javaBeans.FileEntity;
import core.javaBeans.FileState;
import core.javaBeans.RepositoryEntity;
import core.service.FileService;
import core.service.RepositoryService;

@RestController
@RequestMapping("/upload")
public class UploadApi {

	@Autowired
	private FileService fileService;
	@Autowired
	private RepositoryService repositoryService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@RequestMapping("/createfile")
	public @ResponseBody long upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file,
			 @RequestParam("bean") String bean) throws IOException {
		BeanBinaryFile jsonToBean = new ObjectMapper().readValue(bean, BeanBinaryFile.class);
		byte[] dataFile = new byte[(int) file.getSize()];
		file.getInputStream().read(dataFile);		
		return fileService.upload(jsonToBean, dataFile);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping("/remove")
	public @ResponseBody void remove(HttpServletRequest httpServletRequest, @RequestBody BeanBinaryFile bean) throws IOException {
		 fileService.remove(bean);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping("/sync/{id}")
	@Transactional(propagation = Propagation.REQUIRED)
	public @ResponseBody List<BeanBinaryFile> upload(@RequestBody List<BeanBinaryFile> clientFiles, @PathVariable("id") long repositoryId) throws IOException {
		return repositoryService.syncRepository(repositoryId, clientFiles);
	}
	
	
	@GetMapping(value = "/files/{id}",
			  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)    
	@Transactional(propagation = Propagation.REQUIRED)
    public @ResponseBody byte[] serveFile(@PathVariable long id) {

		return fileService.get(id).getContent();
    }
	
	
}
