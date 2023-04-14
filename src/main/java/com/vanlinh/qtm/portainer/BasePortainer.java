package com.vanlinh.qtm.portainer;

import com.ftl.dictionary.Dictionary;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vanlinh.qtm.utils.LoggerUtils;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: </p>
 * <p>Copyright (c) 2022</p>
 * <p>Company: </p>
 *
 * @author Nguyen Van Linh
 * @version 1.0
 */
public class BasePortainer
{
	public static BasePortainer instance;
	public static String JWT;
	public String BASE_API_URL;
	public Gson gson = new Gson();
	public MediaType mediaType;
	OkHttpClient client;

	Dictionary config = new Dictionary("conf/config.dic");

	public static BasePortainer getInstance() throws Exception {
		if(instance == null)
			instance = new BasePortainer();
		return instance;
	}

	private BasePortainer() throws Exception {
		BASE_API_URL = config.getString("Portainer.API_URL");
		client = new OkHttpClient().newBuilder().
				addInterceptor(new JwtExpiredInterceptor()).build();
		mediaType = MediaType.parse("application/json");
	}

	public List<PortainerNodeResponse> getAllPortainerNodes() throws IOException {
		Request request = new Request.Builder()
				.url(BASE_API_URL + "/endpoints")
				.method("GET", null)
				.addHeader("Authorization", "Bearer " + JWT)
				.build();
		Response response = client.newCall(request).execute();
		return gson.fromJson(response.body().string(), new TypeToken<List<PortainerNodeResponse>>() {
		}.getType());
	}


	public String getExecId(int nodeId, String containerName, String urlFile) throws IOException {
		String body = "{" +
				"\r\n  \"AttachStdin\": false," +
				"\r\n  \"AttachStdout\": true," +
				"\r\n  \"AttachStderr\": true," +
				"\r\n  \"DetachKeys\": \"ctrl-p,ctrl-q\"," +
				"\r\n  \"Tty\": true," +
				"\r\n  \"Cmd\": [\"/bin/sh\",\"-c\",\"cat " + urlFile + "\"]\r\n" +
				"}";
		RequestBody requestBody = RequestBody.create(body, mediaType);
		Request request = new Request.Builder()
				.url(BASE_API_URL + "/endpoints/" + nodeId + "/docker/containers/" + containerName + "/exec")
				.method("POST", requestBody)
				.addHeader("Authorization", "Bearer " + JWT)
				.addHeader("Content-Type", "application/json")
				.build();
		Response response = client.newCall(request).execute();
		if (response.code() >= 300)
			throw new FileNotFoundException("File not found");
		return gson.fromJson(response.body().string(), Exec.class).Id;
	}

	public InputStream getFileInContainer(int nodeId, String containerName, String urlFile) throws IOException {
		String execId = getExecId(nodeId, containerName, urlFile);
		String body = "{" +
				"\r\n  \"Detach\": false," +
				"\r\n  \"Tty\": true\r\n" +
				"}";
		RequestBody requestBody = RequestBody.create(body, mediaType);
		Request request = new Request.Builder()
				.url(BASE_API_URL + "/endpoints/" + nodeId + "/docker/exec/" + execId + "/start")
				.method("POST", requestBody)
				.addHeader("Authorization", "Bearer " + JWT)
				.addHeader("Content-Type", "application/json")
				.build();
		Response response = client.newCall(request).execute();
		if (response.code() >=300)
			throw new FileNotFoundException("File not found");
		return response.body().byteStream();
	}

	public List<ContainerResponse> getAllContainerByNodeId(int nodeId) throws IOException {
		Request request = new Request.Builder()
				.url(BASE_API_URL + "/endpoints/" + nodeId + "/docker/containers/json")
				.method("GET", null)
				.addHeader("Authorization", "Bearer " + JWT)
				.build();
		Response response = client.newCall(request).execute();
		if (response.code() == 502)
			return new ArrayList<>();

		return gson.fromJson(response.body().string(), new TypeToken<List<ContainerResponse>>() {
		}.getType());
	}

	private class Exec {
		public String Id;
	}
}
