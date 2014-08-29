package com.spaaza.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spaaza.api.v1.V1Auth;
import com.spaaza.api.v1.V1Internal;
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

	private HttpClient httpClient;

	public Client(String endpoint) {
		this.endpoint = endpoint + "v1/";
		this.setAuth(null);
		this.v1 = new V1();
		this.httpClient = HttpClientBuilder.create().build();
	}

	public APIResponse request(String method, String path,
			Map<String, String> data) throws IOException, APIException {
		
		HttpUriRequest request = null;
		
		String url = this.endpoint + path;
		
		if ("GET".equals(method)) {
			request = new HttpGet(url+getRequestQueryString(data));
		} else if ("POST".equals(method)) {
			request = new HttpPost(url);
			if (data != null) {
				ArrayList<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
				for (Entry<String, String>  e: data.entrySet()) {
					postData.add(new BasicNameValuePair(e.getKey(), e.getValue()));
				}
				((HttpPost)request).setEntity(new UrlEncodedFormEntity(postData));
			}
		} else {
			throw new RuntimeException("Unsupported request method: " + method);
		}
		this.setSpaazaHeaders(request);

		HttpResponse response = httpClient.execute(request);
		
		if (response.getEntity() == null) {
			throw new RuntimeException("Request returned no response body");
		}
		
		String body = IOUtil.toString(response.getEntity().getContent());

		APIResponse apiResponse = mapper.readValue(body, APIResponse.class);
		if (apiResponse.errors != null) {
			throw new APIException(apiResponse.getFirstError());
		}
		return apiResponse;
	}

	private String getRequestQueryString(Map<String, String> data) {
		if (data == null || data.isEmpty()) {
			return "";
		}
		List<String> pairs = new ArrayList<String>(data.size());
		for (Entry<String, String> e : data.entrySet()) {
			try {
				pairs.add(URLEncoder.encode(e.getKey(), "UTF-8") + "=" + URLEncoder.encode(e.getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		return "?" + StringUtils.join(pairs.iterator(), "&");
	}

	private void setSpaazaHeaders(HttpRequest request) {
		if (this.auth != null) {
			request.addHeader("Session-Key", this.auth.getSessionKey());
			request.addHeader("Session-User-Id", this.auth.getUserId());
		}
		// X-MyPrice-App-Hostname
		if (this.appHostname != null) {
			request.addHeader("X-MyPrice-App-Hostname", this.appHostname);
		}

		// X-Spaaza-Request
		request.addHeader("X-Spaaza-Request", "{\"is_native\":true}");
		
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
		public V1Internal internal;

		protected V1() {
			this.pub = new V1Public(Client.this);
			this.auth = new V1Auth(Client.this);
			this.internal = new V1Internal(Client.this);
		}
	}

}
