package com.spaaza.api.v1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Method;

import com.spaaza.api.APIException;
import com.spaaza.api.APIResponse;
import com.spaaza.api.Auth;
import com.spaaza.api.Client;

public class V1Internal {
	private Client client;

	public V1Internal(Client client) {
		this.client = client;
	}
	
	public Auth signup(String username, String firstName, String lastName, String password, String birthday, String gender, String verificationUrlTemplate) throws IOException, APIException {
		Map<String, String> args = new HashMap<String, String>();
		args.put("username", username);
		args.put("first_name", firstName);
		args.put("last_name", lastName);
		args.put("password", password);
		args.put("birthday", birthday);
		args.put("gender", gender);
		args.put("verification_url_template", verificationUrlTemplate);
		APIResponse r = client.request(Method.POST, "internal/signup.json", args);
		return Auth.fromAPIResponse(r);
	}

	public Auth loginFB(String accessToken) throws IOException, APIException {
		Map<String, String> args = new HashMap<String, String>();
		args.put("fb_access_token", accessToken);
		args.put("fb_access_token_expires", "7200");
		APIResponse r = client.request(Method.POST, "internal/login-fb.json", args);
		return Auth.fromAPIResponse(r);
	}
	
}
