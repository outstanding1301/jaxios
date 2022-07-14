package com.outstandingboy.jaxios;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true, fluent = true)
public class HttpResponse<T> {
	private int statusCode;
	private String message;
	private T body;
	private Map<String, String> headers;
}
