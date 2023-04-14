package com.vanlinh.qtm.portainer;

import com.ftl.dictionary.Dictionary;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

/**
 * <p>Title: </p>
 * <p>Copyright (c) 2022</p>
 * <p>Company: </p>
 *
 * @author Nguyen Van Linh
 * @version 1.0
 */
public class JwtExpiredInterceptor implements Interceptor
{
	public String USER_NAME;
	public String PASSWORD;
	public String BASE_API_URL;

	public JwtExpiredInterceptor() throws Exception
	{
		Dictionary config = new Dictionary("conf/config.dic");
		BASE_API_URL = config.getString("Portainer.API_URL");
		USER_NAME = config.getString("Portainer.USER_NAME");
		PASSWORD = config.getString("Portainer.PASSWORD");
	}

	public String getJwt() throws IOException
	{
		Gson gson = new Gson();
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		MediaType mediaType = MediaType.parse("application/json");
		String body = "{\r\n  \"password\": \"" + PASSWORD + "\",\r\n  \"username\": \"" + USER_NAME + "\"\r\n}";
		RequestBody requestBody = RequestBody.create(body, mediaType);
		Request request = new Request.Builder()
				.url(BASE_API_URL + "/auth")
				.method("POST", requestBody)
				.addHeader("Content-Type", "application/json")
				.build();
		Response response = client.newCall(request).execute();
		return gson.fromJson(response.body().string(), JsonWebToken.class).jwt;
	}

	@Override
	public Response intercept(Chain chain) throws IOException
	{
		Request request = chain.request();
		Response response = chain.proceed(request);
		if (response.code() == 401)
		{
			BasePortainer.JWT = getJwt();
			Request newRequest = request.newBuilder().
					header("Authorization", "Bearer " + BasePortainer.JWT).build();
			response.close();
			response = chain.proceed(newRequest);
		}
		return response;
	}

	class JsonWebToken
	{
		public String jwt;
	}
}
