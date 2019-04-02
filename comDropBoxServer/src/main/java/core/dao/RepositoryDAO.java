package core.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import core.javaBeans.RepositoryEntity;
@Repository
public class RepositoryDAO {
	
	
	@PersistenceContext(unitName = "demo")
	private EntityManager entityManager;

	@Transactional(propagation = Propagation.REQUIRED)
	public RepositoryEntity read(long id) {
		
		return	entityManager.find(RepositoryEntity.class, id);
	}

	
	@Transactional(propagation = Propagation.REQUIRED)
	public long create(RepositoryEntity repositoryEntity) {
		System.out.println("fromDAO Create repositoryEntity : " + repositoryEntity.toString());
		entityManager.persist(repositoryEntity);
		return 0;
	}


//	public List<FileEntity> getRepositoryFiles()
}
