package com.outstandingboy.jaxios.mapper;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class GsonJsonMapper implements JsonMapper {
	private final Gson gson;
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final GsonJsonMapper defaultMapper = new GsonJsonMapper(
		new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
			@Override
			public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context)
				throws JsonParseException {
				return LocalDateTime.parse(json.getAsString(), formatter);
			}
		}).registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
			@Override
			public JsonElement serialize(LocalDateTime localDateTime, Type type,
										 JsonSerializationContext jsonSerializationContext) {
				return new JsonPrimitive(formatter.format(localDateTime));
			}
		}).create());

	public static GsonJsonMapper defaultMapper() {
		return defaultMapper;
	}

	@Override
	public String toJson(Object obj) {
		return gson.toJson(obj);
	}

	@Override
	public <T> T fromJson(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}
}
