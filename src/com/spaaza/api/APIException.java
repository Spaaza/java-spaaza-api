package com.spaaza.api;

import com.spaaza.api.APIResponse.Error;

public class APIException extends Exception {

	private static final long serialVersionUID = -2636963117236767557L;

	public APIException(Error firstError) {
		super(firstError.name + ": " + firstError.description);
	}

}
