package com.spaaza.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class Auth {

	@JsonProperty("session_key")
	private String sessionKey;
	
	@JsonProperty("user_id")
	private String userId;
	
	public Auth() {
	}
	
	public Auth(String userId, String sessionKey) {
		this.userId = userId;
		this.sessionKey = sessionKey;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public static Auth fromAPIResponse(APIResponse r) {
		JsonNode u = r.results.get("user_info");
		return new Auth(u.get("id").asText(), r.results.get("session_info").get("session_key").asText());
	}
}
