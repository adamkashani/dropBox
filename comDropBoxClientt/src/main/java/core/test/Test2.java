package core.test;

import core.javaBeans.FileProperties;

public class Test2 {

	public static void main(String[] args) {

//		try {
//			new File("test1").createNewFile();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println(FileProperties.getRoot());
		System.out.println(FileProperties.getRepostoryUrls());
		System.out.println(FileProperties.getRepostoryIds());

	}

}
