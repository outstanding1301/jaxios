package com.outstandingboy.jaxios.mapper;

public interface JsonMapper {
	static JsonMapper defaultMapper() {
		return GsonJsonMapper.defaultMapper();
	}

	String toJson(Object obj);
	<T> T fromJson(String json, Class<T> clazz);
}
