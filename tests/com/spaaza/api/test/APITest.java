package com.spaaza.api.test;

import java.io.IOException;

import com.spaaza.api.APIException;
import com.spaaza.api.Auth;
import com.spaaza.api.Client;

import junit.framework.TestCase;

public class APITest extends TestCase {
	
	private Client client;

	@Override
	protected void setUp() throws Exception {
		client = new Client("http://spaaza.test/");
	}

	public void testIt() throws IOException, APIException {
		client.v1.pub.ping();
	}
	
	public void testLogin() throws IOException, APIException {
		Auth auth = client.v1.auth.login("admin@spaaza.com", "admin123");
		
		assertTrue(auth.getUserId().length() > 0);
		assertTrue(auth.getSessionKey().length() > 0);
	}
}
