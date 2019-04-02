package core.javaBeans;

import java.util.Arrays;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "file")
public class FileEntity {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private long id;

//	@Column(name = "repository_id")
//	private long repositoryId;
	@ManyToOne(fetch = FetchType.EAGER)
	private RepositoryEntity repository;

	@Column(name = "pathName", nullable = false)
	private String pathName;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "content", nullable = false)
	private byte[] content;

	@Column(name = "lastModified", nullable = false)
	private long lastModified;

	public FileEntity() {
		super();
	}

	
	
	public FileEntity(long id, BeanBinaryFile bean) {
		super();
		this.id = id;
		this.repository = new RepositoryEntity(bean.getRepostoryId());
		this.pathName = bean.getRelativePath();
		this.lastModified = bean.getLastModified();
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public RepositoryEntity getRepository() {
		return repository;
	}

	public void setRepository(RepositoryEntity repository) {
		this.repository = repository;
	}

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	@Override
	public String toString() {
		return "FileEntity [id=" + id + ", repository=" + repository + ", pathName=" + pathName + ", content="
				+ Arrays.toString(content) + ", lastModified=" + lastModified + "]";
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileEntity other = (FileEntity) obj;
		if (id != other.id)
			return false;
		if (pathName == null) {
			if (other.pathName != null)
				return false;
		} else if (!pathName.equals(other.pathName))
			return false;
		if (repository == null) {
			if (other.repository != null)
				return false;
		} else if (!repository.equals(other.repository))
			return false;
		return true;
	}

	
}
