package com.becoder.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.becoder.handler.GenericResponse;

public class CommonUtils {

	public static ResponseEntity<?> createBuildResponse(Object data, HttpStatus status) {

		GenericResponse genericResponse = GenericResponse.builder().responseStatus(status).status("success")
				.message("success").data(data).build();

		return genericResponse.create();

	}

	public static ResponseEntity<?> createBuildResponseMessage(String message, HttpStatus status) {

		GenericResponse genericResponse = GenericResponse.builder().responseStatus(status).status("success")
				.message("success").build();

		return genericResponse.create();
	}

	public static ResponseEntity<?> createErrorResponse(Object data, HttpStatus status) {

		GenericResponse genericResponse = GenericResponse.builder().responseStatus(status).status("failed")
				.message("failed").data(data).build();

		return genericResponse.create();

	}

	public static ResponseEntity<?> createErrorResponseMessage(String message, HttpStatus status) {

		GenericResponse genericResponse = GenericResponse.builder().responseStatus(status).status("failed")
				.message("failed").build();

		return genericResponse.create();

	}

}
