package com.spaaza.api;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spaaza.api.v1.V1Auth;
import com.spaaza.api.v1.V1Public;

public class Client {

	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		getMapper().configure(
				DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public V1 v1;

	private String endpoint;
	private String appHostname;

	private Auth auth;

	public Client(String endpoint) {
		this.endpoint = endpoint + "v1/";
		this.setAuth(null);
		this.v1 = new V1();
	}

	public APIResponse request(Method method, String path,
			Map<String, String> data) throws IOException, APIException {
		Connection connection = Jsoup.connect(this.endpoint + path).method(
				method).ignoreContentType(true).ignoreHttpErrors(true);
		if (data != null) {
			connection.data(data);
		}
		this.setSpaazaHeaders(connection);

		Response response = connection.execute();
		String body = response.body();

		APIResponse apiResponse = mapper.readValue(body, APIResponse.class);
		if (apiResponse.errors != null) {
			throw new APIException(apiResponse.getFirstError());
		}
		return apiResponse;
	}

	private void setSpaazaHeaders(Connection connection) {
		if (this.auth != null) {
			connection.header("Session-Key", this.auth.getSessionKey());
			connection.header("Session-User-Id", this.auth.getUserId());
		}
		// X-MyPrice-App-Hostname
		if (this.appHostname != null) {
			connection.header("X-MyPrice-App-Hostname", this.appHostname);
		}

		// TO DO: X-Spaaza-Request
		// TO DO: X-Spaaza-UserCookie
	}

	public Auth getAuth() {
		return auth;
	}

	public void setAuth(Auth auth) {
		this.auth = auth;
	}

	public static ObjectMapper getMapper() {
		return mapper;
	}

	public String getAppHostname() {
		return appHostname;
	}

	public void setAppHostname(String appHostname) {
		this.appHostname = appHostname;
	}

	public class V1 {
		public V1Public pub;
		public V1Auth auth;

		protected V1() {
			this.pub = new V1Public(Client.this);
			this.auth = new V1Auth(Client.this);
		}
	}

}
