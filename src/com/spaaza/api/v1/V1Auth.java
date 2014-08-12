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
		
		JsonNode u = r.results.get("user_info");
		return new Auth(u.get("id").asText(), r.results.get("session_info").get("session_key").asText());
	}

}
