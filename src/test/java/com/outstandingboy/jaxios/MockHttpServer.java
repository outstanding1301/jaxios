package com.outstandingboy.jaxios;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.List;

public class MockHttpServer implements AutoCloseable {

	@RequiredArgsConstructor(staticName = "of")
	public static class Context {
		final HttpMethod method;
		final String path;
		final HttpHandler handler;
	}

	HttpServer server;
	int port;

	@SneakyThrows
	private MockHttpServer(List<Context> contexts) {
		port = availablePort();
		server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
		contexts.forEach(context -> server.createContext(context.path, exchange -> {
			if (exchange.getRequestMethod().equalsIgnoreCase(context.method.name())) {
				context.handler.handle(exchange);
			}
		}));
		server.start();
	}

	public static MockHttpServer of(List<Context> contexts) {
		return new MockHttpServer(contexts);
	}

	@Override
	public void close() {
		server.stop(0);
	}

	@SneakyThrows
	private Integer availablePort() {
		try (ServerSocket socket = new ServerSocket(0)) {
			return socket.getLocalPort();
		}
	}

	@SneakyThrows
	public static void response(HttpExchange exchange, String text) {
		exchange.sendResponseHeaders(200, text.length());
		OutputStream os = exchange.getResponseBody();
		os.write(text.getBytes());
	}

	@SneakyThrows
	public static String body(HttpExchange exchange) {
		return new String(exchange.getRequestBody().readAllBytes());
	}
}
