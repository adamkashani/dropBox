//package text;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.Scanner;
//
//import core.dao.FileDAO;
//import core.javaBeans.FileText;
//
//public class Main {
//	public static void main(String[] args) {
//		String pathRoot = "C:/targil";
//		showFileName(pathRoot);
//	}
//
//	public static void showFileName(String pathRoot) {
//
//		File rootFile = new File(pathRoot);
//		FileDAO fileToDb = new FileDAO();
//		File[] fileList = rootFile.listFiles();
//		System.out.println(fileList);
//		for (File file : fileList) {
//			String absolutepath = file.getAbsolutePath();
//			System.out.println(absolutepath);
//			if (absolutepath.endsWith(".txt")) {
//				FileText fileText = new FileText();
//				// create to date base
//				try {
//					Scanner scanner = new Scanner(new File(absolutepath));
//					String allTextFile = null;
//					while (scanner.hasNextLine()) {
//						allTextFile += scanner.nextLine();
//					}
//					fileText.setText(allTextFile);
//					fileText.setPathName(absolutepath);
//					fileToDb.create(fileText);
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
//			} else {
//				// if the file not end with txt
//				showFileName(absolutepath);
//			}
//
//		}
//	}
//
//}
