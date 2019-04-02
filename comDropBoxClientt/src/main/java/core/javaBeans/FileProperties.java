package core.javaBeans;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class FileProperties {

	final private static String PROPERTIES_ROOT = "root.properties";
	final private static String PROPERTIES_FILE_PATH = "repostory.properties";
	private static Properties properties;
	private static Map<Long, String> repostoryUrls;
	private static Set<String> repostoryIds;
	static {
		init();
	}

	private static void init() {
		properties = new Properties();
		repostoryUrls = new HashMap<>();
		getAll();
	}

	public static String getProperty(String name) {
		try (InputStream inputStream = new FileInputStream(PROPERTIES_FILE_PATH);) {
			properties.load(inputStream);
			return properties.getProperty(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void setProperty(String name, String value) {
		// TODO לחזור לסדר כתיבה לקובץ
//		try(OutputStream)
		properties.put(name, value);
	}

	private static void getAll() {
		try (InputStream inputStream = new FileInputStream(PROPERTIES_FILE_PATH);) {
			properties.load(inputStream);
			repostoryIds = properties.stringPropertyNames();
			for (String string : repostoryIds) {
				repostoryUrls.put(Long.parseLong(string), getProperty(string));
			}
			inputStream.close();
		} catch (IOException e) {
		}
	}

	public static String getRoot() {
		try (InputStream inputStream = new FileInputStream(PROPERTIES_ROOT);) {
			properties.load(inputStream);
			return properties.getProperty("ROOT");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Map<Long, String> getRepostoryUrls() {
		return repostoryUrls;
	}

	public static long[] getRepostoryIds() {
		long[] ids = new long[repostoryIds.size()];
		String[] string = new String[repostoryIds.size()];
		repostoryIds.toArray(string);
		for (int i = 0; i < ids.length; i++) {
			ids[i] = Long.parseLong(string[i]);
		}
		return ids;
	}

}
