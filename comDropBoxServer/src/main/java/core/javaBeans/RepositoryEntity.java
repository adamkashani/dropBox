package core.javaBeans;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "repository")
public class RepositoryEntity {

	@Id
	@GeneratedValue
	private long id;

	@OneToMany(mappedBy = "repository")
	private List<FileEntity> files = new ArrayList<FileEntity>();

	public RepositoryEntity() {
		super();
	}

	public RepositoryEntity(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<FileEntity> getFiles() {
		return files;
	}

	public void setFiles(List<FileEntity> files) {
		this.files = files;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RepositoryEntity other = (RepositoryEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
