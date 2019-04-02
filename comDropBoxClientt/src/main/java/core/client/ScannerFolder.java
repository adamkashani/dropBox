package core.client;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import core.javaBeans.BeanBinaryFile;
import core.javaBeans.FileProperties;
import core.javaBeans.FileState;

public class ScannerFolder {

	private static String dropBoxRootPath;
	static long[] repositoryIdsArray = FileProperties.getRepostoryIds();
	private static Map<Long, String> repositories = FileProperties.getRepostoryUrls();
	public List<BeanBinaryFile> allFiles = new ArrayList<>();
	private WatchService watchService;

	public ScannerFolder(String dropBoxRootPath, WatchService watchService) {
		super();
		this.dropBoxRootPath = dropBoxRootPath;
		this.watchService = watchService;

	}

	public static String getRepositoryPath(long repositoryId) {
		return repositories.get(repositoryId);
	}

	public static String getAbsolutRepositoryPath(long repositoryId) {
		return dropBoxRootPath + getRepositoryPath(repositoryId);
	}

	public static BeanBinaryFile getBean(File file, long repositoryId) {
		BeanBinaryFile bean = new BeanBinaryFile();
//		bean.setId(id);
		bean.setLastModified(file.lastModified());
		bean.setRelativePath(file.getAbsolutePath().substring(getAbsolutRepositoryPath(repositoryId).length()));
		bean.setRepostoryId(repositoryId);
		return bean;
	}

	public static long getRepositoryId(String path) {
//		path.toAbsolutePath().toString().substring()
//		return dropBoxRootPath + repositories.get(repositoryId);
		for (int i = 0; i < repositoryIdsArray.length; i++) {
			if (path.startsWith(getAbsolutRepositoryPath(repositoryIdsArray[i]))) {
				return repositoryIdsArray[i];
			}
		}
		return 0;
	}

	// ליפני הקריאה למתודה צריך לאפס את המערך של הקבצים
	public void scanListFile(File rootFolder, long repositoryId) {
		File[] fileList = rootFolder.listFiles();
		try {
			Paths.get(rootFolder.getAbsolutePath()).register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (fileList == null) {
			return;
		}
		for (File file : fileList) {
			if (file.isFile()) {
				BeanBinaryFile bean = getBean(file, repositoryId);
				bean.setState(FileState.SYNC);
				this.allFiles.add(bean);
			} else if (file.isDirectory()) {
				scanListFile(file, repositoryId);
			} else {
				// Exception smiting not correct
			}
			// if the file not end with txt
		}
	}

	public List<BeanBinaryFile> startScan(long repositoryId) {
		this.allFiles.clear();
//		for (Long repositoryId : repositoriesId) {
		File repositoryRoot = new File(getAbsolutRepositoryPath(repositoryId));
		scanListFile(repositoryRoot, repositoryId);
//		}
		return allFiles;
	}

}
