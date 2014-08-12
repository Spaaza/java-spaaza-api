package com.spaaza.api.test;

import java.io.IOException;

import com.spaaza.api.APIException;
import com.spaaza.api.Auth;
import com.spaaza.api.Client;
import com.spaaza.api.v1.auth.LoginStatusResponse;

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

	public void testGetLoginStatus() throws IOException, APIException {
		client.setAuth(client.v1.auth.login("admin@spaaza.com", "admin123"));
		
		LoginStatusResponse s = client.v1.auth.getLoginStatus();
		assertEquals("admin@spaaza.com", s.username);
	}
	
	public void testSignup() throws IOException, APIException {
		//client.v1.internal.signup("foo@bar.com", "Arjan", "test", "aapaap", "1999-01-01", "F", "xxx");
	}
	
}
