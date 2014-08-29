package com.spaaza.api.v1;

import java.io.IOException;

import com.spaaza.api.APIException;
import com.spaaza.api.Client;

public class V1Public {
	
	private Client client;

	public V1Public(Client client) {
		this.client = client;
	}
	
	public void ping() throws IOException, APIException {
		client.request("GET", "public/ping.json", null);
	}
}