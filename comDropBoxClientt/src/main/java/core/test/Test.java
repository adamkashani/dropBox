package core.test;

import java.util.List;

import core.client.HttpClient;
import core.client.ScannerFolder;
import core.javaBeans.BeanBinaryFile;
import core.javaBeans.FileProperties;
import core.javaBeans.FileState;
import static java.nio.file.StandardWatchEventKinds.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;

public class Test {
	static String pathTofile = FileProperties.getRoot();
	static WatchService watchService;
	static ScannerFolder scanner;
	static HttpClient httpClient = new HttpClient();
	static {
		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scanner = new ScannerFolder(pathTofile, watchService);
	}

	public Test() {
		System.out.println("constructor");
	}

	public static void main(String[] args) {

		long[] repositoryId = FileProperties.getRepostoryIds();

		for (int i = 0; i < repositoryId.length; i++) {
			List<BeanBinaryFile> responseList = httpClient.syncFolders(scanner.startScan(repositoryId[i]),
					repositoryId[i]);
			for (BeanBinaryFile beanBinaryFile : responseList) {
				if (beanBinaryFile.getState() == FileState.UPLOAD || beanBinaryFile.getState() == FileState.UPDATE) {
					httpClient.uploadFile(beanBinaryFile);
				}
				if (beanBinaryFile.getState() == FileState.DOWNLOAD || beanBinaryFile.getState() == FileState.NEW) {
					httpClient.getFile(beanBinaryFile);
				}
				if (beanBinaryFile.getState() == FileState.DELETE) {
					httpClient.deleteFile(beanBinaryFile);
					File file = new File(scanner.getAbsolutRepositoryPath(beanBinaryFile.getRepostoryId())
							+ beanBinaryFile.getRelativePath());
					if (file.delete()) {
						System.out.println("File deleted successfully");
					} else {
						System.err.println("File delete failed");
					}
				}

			}
		}

//		try {
//			registerRecursive(Paths.get(pathTofile));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		startListening();
	}

	private static void startListening() {
		try {
			System.out.println("from startListening");
			Path path = Paths.get(pathTofile);

			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);

			WatchKey key;
			while ((key = watchService.take()) != null) {
				System.out.println("from watchService.take()) while");

				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					System.out.println("Event kind:" + kind + ". File affected: " + event.context() + ".");

					// This key is registered only
					// for ENTRY_CREATE events,
					// but an OVERFLOW event can
					// occur regardless if events
					// are lost or discarded.
					if (kind == OVERFLOW) {
						continue;
					}

					// The filename is the
					// context of the event.
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path filename = ev.context();
					System.out.println("Path file name filename  " + filename);
					// Verify that the new
					// file is a text file.
//			                try {
					// Resolve the filename against the directory.
					// If the filename is "test" and the directory is "foo",
					// the resolved name is "test/foo".
					Path item = (Path) event.context();
					File file = new File(
							((Path) key.watchable()).toAbsolutePath() + File.separator + item.getFileName());
					System.out.println("Path file name child  " + file.getAbsolutePath());
//			                    System.out.println("Path file name child  " + ((Path) key.watchable()).getRoot());
					long repositoryId = scanner.getRepositoryId(file.getAbsolutePath());
					if (file.isDirectory()) {

						if (kind == ENTRY_MODIFY) {
//			                    		scanner.scanListFile(file, repositoryId);
							// Scan subfolders
							System.out.println("Folder Modify");
							continue;
						}
						if (kind == ENTRY_CREATE) {
							System.out.println("Folder Create");
							scanner.allFiles.clear();
							scanner.scanListFile(file, repositoryId);
							httpClient.uploadFiles(scanner.allFiles);

							continue;
						}
						if (kind == ENTRY_DELETE) {
							System.out.println("Folder Delete");
							// delete all tree
//			                    		List<BeanBinaryFile> responseList = httpClient.syncFolders(scanner.startScan(repositoryId),repositoryId);
//			                			for (BeanBinaryFile beanBinaryFile : responseList) {
//			                				if(beanBinaryFile.getState()==FileState.NEW) {
//			                					httpClient.deleteFile(beanBinaryFile);
//			                				}
//			                			}
							continue;
						}
					} else {
						// need to get repository or better id

//			                    	if(file.exists()) {
						BeanBinaryFile beanFile = scanner.getBean(file, repositoryId);
//			                    	}
						if (kind == ENTRY_MODIFY) {
							if (!file.exists()) {
								System.out.println("file doesn't exist");
								httpClient.deleteFile(beanFile);
								continue;
							}
							System.out.println("File update");
							beanFile.setState(FileState.UPDATE);
							httpClient.uploadFile(beanFile);
							continue;
						}
						if (kind == ENTRY_CREATE) {
							System.out.println("File Created");
							beanFile.setState(FileState.UPLOAD);
							httpClient.uploadFile(beanFile);
							continue;
						}
						if (kind == ENTRY_DELETE) {
							System.out.println("File Delete");
							beanFile.setState(FileState.DELETE);
							httpClient.deleteFile(beanFile);
							continue;
						}
					}
//			                    if (!Files.probeContentType(child).equals("text/plain")) {
//			                        System.err.format("New file '%s'" +
//			                            " is not a plain text file.%n", filename);
//			                        continue;
//			                    }
//			                } catch (IOException x) {
//			                    System.err.println(x);
//			                    continue;
//			                }

					// Email the file to the
					// specified email alias.
//			                System.out.format("Emailing file %s%n", filename);
					// Details left to reader....
				}

				// Reset the key -- this step is critical if you want to
				// receive further watch events. If the key is no longer valid,
				// the directory is inaccessible so exit the loop.
				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}
		} catch (IOException x) {
			System.err.println(x);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.err.println(e);
//			e.printStackTrace();
		}
	}

	static private void registerRecursive(final Path root) throws IOException {
		// register all subfolders
		Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				System.out.println(dir);
				System.out.println(attrs);
				dir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
				return FileVisitResult.CONTINUE;
			}
		});
	}
}
