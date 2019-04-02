package core.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import core.dao.IDAOFile;
import core.dao.RepositoryDAO;
import core.javaBeans.BeanBinaryFile;
import core.javaBeans.FileEntity;
import core.javaBeans.FileState;
import core.javaBeans.RepositoryEntity;

@Controller
public class RepositoryService {
	@Autowired
	//צריך להיזהר ממצב שכמה מחלקות יורשות מאותו DAO
	private RepositoryDAO repositoryDao; 
	
	
	public long create (RepositoryEntity repositoryEntity){
		return repositoryDao.create(repositoryEntity);		
		
	}
	
	public RepositoryEntity get(long id) {
		return repositoryDao.read(id);
	}

	public List<BeanBinaryFile> syncRepository(long repositoryId, List<BeanBinaryFile> clientFiles) {
		List<BeanBinaryFile> syncFiles = new ArrayList<BeanBinaryFile>();
		List<BeanBinaryFile> clientFilesCopy = new ArrayList<BeanBinaryFile>(clientFiles);
		List<FileEntity> repositoryFiles = new ArrayList<FileEntity>();
		repositoryFiles = this.get(repositoryId).getFiles();
		List<FileEntity> repositoryFilesCopy = new ArrayList<FileEntity>(repositoryFiles);
//		repositoryFiles = repositoryService.getFiles(repositoryId);
//		repositoryFiles.remove(clientFiles);
	
		//Compare files
		//files to update
		for (FileEntity repoFile : repositoryFilesCopy) {
			for (BeanBinaryFile clientFile : clientFilesCopy) {
				if(repoFile.getPathName().equals(clientFile.getRelativePath())) {
					if(repoFile.getLastModified()>clientFile.getLastModified()) {
						clientFile.setState(FileState.DOWNLOAD);
						syncFiles.add(clientFile);
					}else if(repoFile.getLastModified()<clientFile.getLastModified()) {
						clientFile.setState(FileState.UPDATE);
						syncFiles.add(clientFile);
					}
					clientFile.setId(repoFile.getId());
					clientFiles.remove(clientFile);	
					repositoryFiles.remove(repoFile);
				}
			}
		}
		for (FileEntity repoFile : repositoryFiles) {
			BeanBinaryFile beanFile = new BeanBinaryFile(repoFile.getId(), repositoryId, repoFile.getLastModified(), repoFile.getPathName(), FileState.NEW);
			syncFiles.add(beanFile);
		}
		for (BeanBinaryFile clientFile : clientFiles) {
//			clientFile.setState(FileState.DELETE);
			clientFile.setState(FileState.DELETE);
			syncFiles.add(clientFile);
			
		}
		return syncFiles;
	}
}