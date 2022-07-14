package com.outstandingboy.jaxios.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outstandingboy.jaxios.exception.JaxiosMappingException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JacksonMapper implements JsonMapper {
	private final ObjectMapper mapper;

	@Override
	public String toJson(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new JaxiosMappingException(e);
		}
	}

	@Override
	public <T> T fromJson(String json, Class<T> clazz) {
		try {
			return mapper.readValue(json, clazz);
		} catch (Throwable e) {
			throw new JaxiosMappingException(e);
		}
	}
}
