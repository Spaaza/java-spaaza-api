package com.spaaza.api.v1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Method;

import com.fasterxml.jackson.databind.JsonNode;
import com.spaaza.api.APIException;
import com.spaaza.api.APIResponse;
import com.spaaza.api.Auth;
import com.spaaza.api.Client;
import com.spaaza.api.v1.auth.LoginStatusResponse;

public class V1Auth {
	
	private Client client;

	public V1Auth(Client client) {
		this.client = client;
	}
	
	public Auth login(String username, String password) throws IOException, APIException {
		Map<String, String> args = new HashMap<String, String>();
		args.put("username", username);
		args.put("password", password);
		APIResponse r = client.request(Method.POST, "auth/login.json", args);
		return Auth.fromAPIResponse(r);
	}

	public LoginStatusResponse getLoginStatus() throws IOException, APIException {
		if (client.getAuth() == null) {
			throw new RuntimeException("Method must be called with authentication credentials");
		}
		
		Map<String, String> args = new HashMap<String, String>();
		args.put("user_id", client.getAuth().getUserId());
		args.put("session_key", client.getAuth().getSessionKey());
		
		APIResponse r = client.request(Method.GET, "auth/get-login-status.json", args);
		return Client.getMapper().treeToValue(r.results, LoginStatusResponse.class);
	}

}
