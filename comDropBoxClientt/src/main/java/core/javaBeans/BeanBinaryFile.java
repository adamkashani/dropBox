package core.javaBeans;

public class BeanBinaryFile {

	private long repositoryId;
	private String relativePath;
	private long lastModified;

	private FileState state;
	private long id;

	public BeanBinaryFile() {
		super();
	}

	public BeanBinaryFile(long repositoryId, String relativePath) {
		super();
		this.repositoryId = repositoryId;
		this.relativePath = relativePath;
	}
	

	public BeanBinaryFile(long repostoryId, String relativePath, long lastModified, FileState state) {
		super();
		this.repositoryId = repostoryId;
		this.relativePath = relativePath;
		this.lastModified = lastModified;
		this.state = state;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public long getRepostoryId() {
		return repositoryId;
	}

	public void setRepostoryId(long repostoryId) {
		this.repositoryId = repostoryId;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public FileState getState() {
		return state;
	}

	public void setState(FileState state) {
		this.state = state;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BeanBinaryFile other = (BeanBinaryFile) obj;
		if (relativePath == null) {
			if (other.relativePath != null)
				return false;
		} else if (!relativePath.equals(other.relativePath))
			return false;
		if (repositoryId != other.repositoryId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BeanBinaryFile [repositoryId=" + repositoryId + ", relativePath=" + relativePath + ", lastModified="
				+ lastModified + ", state=" + state + ", id=" + id + "]";
	}
	
	

}