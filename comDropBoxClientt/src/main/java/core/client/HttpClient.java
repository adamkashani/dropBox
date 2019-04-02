package core.client;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import core.javaBeans.BeanBinaryFile;

public class HttpClient {

	private String url = "http://localhost:8080/comDropBox/rest";
//	private String url = "http://ec2-18-222-31-108.us-east-2.compute.amazonaws.com:8080/DropBox/rest";

	// to close the connection to server
	protected void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException ex) {

		}
	}

	public void uploadFiles(List<BeanBinaryFile> files) {
		for (BeanBinaryFile bean : files) {
//			String repositoryRoot = ScannerFolder.getAbsolutPath(bean.getRepostoryId());
//			File file = new File(repositoryRoot + bean.getRelativePath());
			uploadFile(bean);
		}
	}

	public void uploadFile(BeanBinaryFile beanBinaryFile) {
		try {

			String repositoryRoot = ScannerFolder.getAbsolutRepositoryPath(beanBinaryFile.getRepostoryId());
			File fileToSend = new File(repositoryRoot + beanBinaryFile.getRelativePath());
			String beanToJson = new ObjectMapper().writeValueAsString(beanBinaryFile);
			String relativePath = new String(beanBinaryFile.getRelativePath().getBytes("UTF-8"));
			HttpEntity httpEntity = MultipartEntityBuilder.create().setCharset(Charset.forName("UTF-8"))
					.addBinaryBody("file", fileToSend, ContentType.APPLICATION_OCTET_STREAM, relativePath)
					.addPart("bean", new StringBody(beanToJson, ContentType.APPLICATION_JSON)).build();
			HttpPost httpPost = new HttpPost(url + "/upload/createfile");

			httpPost.setEntity(httpEntity);
			HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				long id = Long.parseLong(EntityUtils.toString(httpResponse.getEntity()));
//				getFile(id, ScannerFolder.getRepositoryPath(beanBinaryFile.getRepostoryId()) + beanBinaryFile.getRelativePath());
				System.out.println("response success - File updloaded : " + id);
			} else {
				// error from server
//				System.out.println("response filed : " + EntityUtils.toString(httpResponse.getEntity()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getFile(BeanBinaryFile beanBinaryFile) {
		HttpGet httpGet = new HttpGet(url + "/upload/files/" + beanBinaryFile.getId());

		HttpResponse httpResponse;
		try {
			httpResponse = HttpClientBuilder.create().build().execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				byte[] data = EntityUtils.toByteArray(httpResponse.getEntity());
				String repositoryRoot = ScannerFolder.getAbsolutRepositoryPath(beanBinaryFile.getRepostoryId());
				File file = new File(repositoryRoot + beanBinaryFile.getRelativePath());
//				File file = new File("C:\\TempFilesProjects"+beanBinaryFile.getRelativePath());
				String pattern = Pattern.quote(System.getProperty("file.separator"));
				System.out.println(file.getAbsolutePath());
				String[] folders = file.getAbsolutePath().split(pattern);
				String path = folders[0];
				for (int i = 1; i < folders.length - 1; i++) {
					path = path + "\\" + folders[i];
					file = new File(path);
					if (!file.exists()) {
						file.mkdir();
					}
				}
				path = path + "\\" + folders[folders.length - 1];
				file = new File(path);
				if (!file.exists()) {
//					file.mkdirs();
					file.createNewFile();
				}
				System.out.println(file.getAbsolutePath());
				FileOutputStream fileout = new FileOutputStream(file);
				BufferedOutputStream buffout = new BufferedOutputStream(fileout);
				buffout.write(data);
				buffout.flush();
				buffout.close();
				fileout.close();
//				System.out.println("response success finished");

			} else {
				// error from server

				System.out.println("response failed : " + EntityUtils.toString(httpResponse.getEntity()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<BeanBinaryFile> syncFolders(List<BeanBinaryFile> beanBinaryFile, long repository) {
		try {
			String beanToJson = new ObjectMapper().writeValueAsString(beanBinaryFile);
			StringEntity stringEntity = new StringEntity(beanToJson, ContentType.APPLICATION_JSON);
			HttpPost httpPost = new HttpPost(url + "/upload/sync/" + repository);
			httpPost.setEntity(stringEntity);

			HttpResponse httpResponse;
			httpResponse = HttpClientBuilder.create().build().execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				byte[] stringContent = EntityUtils.toByteArray(httpResponse.getEntity());
//			BeanBinaryFile beanFile = new ObjectMapper().readValue(stringContent, BeanBinaryFile.class);
				List<BeanBinaryFile> serverList = new ObjectMapper().readValue(stringContent,
						new TypeReference<List<BeanBinaryFile>>() {
						});
				System.out.println("response success - repository " + repository);
				for (BeanBinaryFile beanFile : serverList) {
					System.out.println(beanFile);
				}
				return serverList;
//			for (byte byte1 : beanFile.getRelativePath().getBytes()) {
//				System.out.println(Byte.toUnsignedInt(byte1));;
//			}
			} else {
				// error from server
				System.out.println("response failed : " + EntityUtils.toString(httpResponse.getEntity()));
				// Change to exception
				return null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void deleteFile(BeanBinaryFile beanBinaryFile) {
		// TODO Auto-generated method stub
		try {
			String beanToJson = new ObjectMapper().writeValueAsString(beanBinaryFile);
			StringEntity stringEntity = new StringEntity(beanToJson, ContentType.APPLICATION_JSON);
			HttpPost httpPost = new HttpPost(url + "/upload/remove");
			httpPost.setEntity(stringEntity);

			HttpResponse httpResponse;
			httpResponse = HttpClientBuilder.create().build().execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() < 300) {
				System.out.println("response success delete");
			} else {
				// error from server
				System.out.println("response failed : " + EntityUtils.toString(httpResponse.getEntity()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
