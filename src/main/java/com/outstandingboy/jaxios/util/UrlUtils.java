package com.outstandingboy.jaxios.util;

public class UrlUtils {
	public static String base(String url) {
		if (url.endsWith("/")) {
			return url.substring(0, url.length() - 1);
		}
		return url;
	}
}
