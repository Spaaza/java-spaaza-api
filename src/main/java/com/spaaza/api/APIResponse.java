package com.spaaza.api;

import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

public class APIResponse {
	
	public  Map<String, Error> errors;
	public JsonNode results;
	
	public static class Error {
		public int code;
		public String name;
		public String description;
	}
	
	public Error getFirstError() {
		Iterator<Error> it = errors.values().iterator();
		return it.hasNext() ? it.next() : null;
	}
}
