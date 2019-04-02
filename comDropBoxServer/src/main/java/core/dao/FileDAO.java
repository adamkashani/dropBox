package core.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import core.javaBeans.BeanBinaryFile;
import core.javaBeans.FileEntity;
import core.javaBeans.RepositoryEntity;

@Repository
public class FileDAO implements IDAOFile {
	@PersistenceContext(unitName = "demo")
	private EntityManager entityManager;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public FileEntity read(long id) {
		return entityManager.find(FileEntity.class, id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public long create(FileEntity fileEntity) {
		entityManager.persist(fileEntity);
		return fileEntity.getId();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(FileEntity fileEntity) {
		entityManager.merge(fileEntity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public FileEntity find(BeanBinaryFile beanFile) {
		TypedQuery<FileEntity> findQuery =
			      entityManager.createQuery("SELECT f FROM FileEntity f WHERE f.pathName=? AND f.repository=?", FileEntity.class);
		findQuery.setParameter(1, beanFile.getRelativePath());
		findQuery.setParameter(2, new RepositoryEntity(beanFile.getRepostoryId()));		
		return findQuery.getSingleResult();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void remove(BeanBinaryFile beanFile) {
		Query findQuery =
				entityManager.createQuery("DELETE FROM FileEntity f WHERE f.pathName=? AND f.repository=?");
		findQuery.setParameter(1, beanFile.getRelativePath());
		findQuery.setParameter(2, new RepositoryEntity(beanFile.getRepostoryId()));		
//		findQuery.setParameter("pathName", beanFile.getRelativePath());
//		findQuery.setParameter("repositoryId", beanFile.getRepostoryId());		
		findQuery.executeUpdate();
	}

}
