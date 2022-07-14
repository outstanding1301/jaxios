package com.outstandingboy.jaxios;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor(staticName = "of")
@Accessors(fluent = true, chain = true)
class JaxiosRequest {
	private static ExecutorService threadPool = Executors.newFixedThreadPool(100);
	private final Jaxios jaxios;
	private final String url;
	private final HttpMethod method;
	private String body;
	private Map<String, String> headers = new LinkedHashMap<>();
	private Map<String, String> params = new LinkedHashMap<>();

	public JaxiosRequest header(String key, String value) {
		headers.put(key, value);
		return this;
	}

	public JaxiosRequest param(String key, Object value) {
		headers.put(key, value.toString());
		return this;
	}

	public JaxiosRequest body(String body) {
		this.body = body;
		return this;
	}

	public JaxiosRequest body(Object body) {
		body(jaxios.mapper().toJson(body));
		return this;
	}

	public String queryString() {
		String param = "";
		if (JaxiosRequest.this.params.size() > 0) {
			param = "?" +
					JaxiosRequest.this.params.entrySet().stream()
						.map(e -> e.getKey() + "=" + e.getValue())
						.collect(Collectors.joining("&"));
		}
		return param;
	}

	public Future<HttpResponse<String>> send() {
		return send(String.class);
	}

	@SneakyThrows
	public <T> Future<HttpResponse<T>> send(Class<T> type) {
		System.out.println("send req");
		CompletableFuture<HttpResponse<T>> future = new CompletableFuture<>();
		threadPool.execute(() -> {
			System.out.println("thread");
			try {
				System.out.println("will execute");
				final HttpResponse<T> httpResponse = jaxios.httpClient().execute(new HttpRequest(), res -> {
					System.out.println("got res");
					HttpResponse<T> response = new HttpResponse<>();
					response.statusCode(res.getStatusLine().getStatusCode());
					response.message(res.getStatusLine().getReasonPhrase());
					response.headers(Arrays.stream(res.getAllHeaders())
						.map(header -> Map.entry(header.getName(), header.getValue()))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new)));
					String body = EntityUtils.toString(res.getEntity());
					response.body(type == String.class ? (T) body : jaxios.mapper().fromJson(body, type));
					return response;
				});
				System.out.println("gotit");
				future.complete(httpResponse);
			} catch (Throwable e) {
				e.printStackTrace();
				future.completeExceptionally(e);
			}
		});
		return future;
	}

	private class HttpRequest extends HttpEntityEnclosingRequestBase {
		public HttpRequest() {
			setURI(URI.create(url + queryString()));
			headers.forEach(this::setHeader);
			if (body != null) {
				setEntity(EntityBuilder.create()
					.setContentType(ContentType.parse(
						headers.getOrDefault("Content-Type", ContentType.APPLICATION_JSON.getMimeType())))
					.setText(body)
					.build());
			}
		}

		@Override
		public String getMethod() {
			return method.name();
		}
	}
}
