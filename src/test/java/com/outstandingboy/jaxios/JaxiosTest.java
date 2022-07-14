package com.outstandingboy.jaxios;

import com.outstandingboy.jaxios.MockHttpServer.Context;
import com.sun.net.httpserver.HttpHandler;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.outstandingboy.jaxios.HttpMethod.*;
import static org.junit.jupiter.api.Assertions.*;

class JaxiosTest {

	@Test
	void test() throws Throwable {
		HttpHandler handler = exchange -> {
			System.out.println(exchange.getRequestURI());
			System.out.println(exchange.getRequestHeaders());
			System.out.println(MockHttpServer.body(exchange));
			exchange.getResponseHeaders().add("Content-Type", "application/json");
			MockHttpServer.response(exchange, "{\"hello\": \"world\"}");
		};
		try (MockHttpServer server = MockHttpServer.of(List.of(Context.of(GET, "/", handler)))) {
			Future<HttpResponse<Map>> future = Jaxios.create().baseUrl("http://localhost:" + server.port)
				.get("/")
				.param("hello", "world")
				.param("kk", 123)
				.body(Map.of("hihi", "zz", "kk", 123))
				.send(Map.class);
			HttpResponse<Map> res = future.get();
			System.out.println("res = " + res);
		}
	}

}
