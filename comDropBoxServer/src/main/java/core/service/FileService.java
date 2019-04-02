package core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import core.dao.IDAOFile;
import core.javaBeans.BeanBinaryFile;
import core.javaBeans.FileEntity;
import core.javaBeans.FileState;
import core.javaBeans.RepositoryEntity;

@Controller
public class FileService {
	
	@Autowired
	//צריך להיזהר ממצב שכמה מחלקות יורשות מאותו DAO
	private IDAOFile daoFile; 
	
	
	public long create (FileEntity fileEntity){
		return daoFile.create(fileEntity);			
		
	}
	
	public FileEntity get(long id) {
		return daoFile.read(id);
	}

	public void update(FileEntity fileEntity) {
		daoFile.save(fileEntity);
	}
	public FileEntity find(BeanBinaryFile bean) {
		return daoFile.find(bean);
	}
		
	public long upload(BeanBinaryFile jsonToBean, byte[] dataFile) {
		FileEntity fileEntity = new FileEntity();
		fileEntity.setContent(dataFile);
		fileEntity.setPathName(jsonToBean.getRelativePath());
		fileEntity.setLastModified(jsonToBean.getLastModified());
		fileEntity.setRepository(new RepositoryEntity(jsonToBean.getRepostoryId()));
		if(jsonToBean.getState()==FileState.UPLOAD) {
			this.create(fileEntity);			
		}else if(jsonToBean.getState()==FileState.UPDATE) {
			fileEntity.setId(find(jsonToBean).getId());
			this.update(fileEntity);
		}
		return fileEntity.getId();
	}

	public void remove(BeanBinaryFile bean) {
		// TODO Auto-generated method stub
		 daoFile.remove(bean);
	}

}
