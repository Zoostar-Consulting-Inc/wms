package net.zoostar.wms.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public interface RestProxyService<T, E> {
	ResponseEntity<E> exchange(String url,
			HttpMethod method, HttpHeaders headers,
			T request, Class<E> responseClass);
}
