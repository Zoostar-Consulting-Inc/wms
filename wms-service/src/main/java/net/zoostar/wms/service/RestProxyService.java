package net.zoostar.wms.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import net.zoostar.wms.web.response.ResponseEntityBean;

public interface RestProxyService<T, E> {
	ResponseEntityBean<E> exchange(String url,
			HttpMethod method, HttpHeaders headers,
			T request, Class<E> responseClass);
}
