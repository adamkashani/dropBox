package core.dao;

import core.javaBeans.BeanBinaryFile;
import core.javaBeans.FileEntity;

public interface IDAOFile {
	
   FileEntity	read (long id);
   
   long	create(FileEntity fileText);

   void save(FileEntity fileEntity);


void remove(BeanBinaryFile beanBinaryFile);

FileEntity  find(BeanBinaryFile beanFile);
   

}
