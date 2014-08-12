package com.spaaza.api;

import com.fasterxml.jackson.annotation.JsonProperty;

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
}
