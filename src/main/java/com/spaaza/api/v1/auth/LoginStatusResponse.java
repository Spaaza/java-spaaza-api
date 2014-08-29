package com.spaaza.api.v1.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginStatusResponse {

	@JsonProperty("user_id")
	public String userId;
	
	@JsonProperty("username")
	public String username;
	
	
}
