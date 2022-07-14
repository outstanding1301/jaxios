package com.outstandingboy.jaxios;

import com.outstandingboy.jaxios.mapper.JsonMapper;
import com.outstandingboy.jaxios.util.UrlUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

@Data
@NoArgsConstructor(staticName = "create")
@Accessors(fluent = true, chain = true)
public class Jaxios {
	private final JsonMapper mapper = JsonMapper.defaultMapper();
	private String baseUrl = "";
	private HttpClient httpClient = HttpClientBuilder.create().build();

	public Jaxios baseUrl(String baseUrl) {
		this.baseUrl = UrlUtils.base(baseUrl);
		return this;
	}

	public JaxiosRequest request(String url, HttpMethod method) {
		return JaxiosRequest.of(this, baseUrl+url, method);
	}

	public JaxiosRequest get(String url) {
		return request(url, HttpMethod.GET);
	}

	public JaxiosRequest post(String url) {
		return request(url, HttpMethod.POST);
	}

	public JaxiosRequest put(String url) {
		return request(url, HttpMethod.PUT);
	}

	public JaxiosRequest patch(String url) {
		return request(url, HttpMethod.PATCH);
	}

	public JaxiosRequest delete(String url) {
		return request(url, HttpMethod.DELETE);
	}
}
