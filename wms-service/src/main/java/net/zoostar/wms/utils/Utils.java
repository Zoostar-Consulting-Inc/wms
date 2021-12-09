package net.zoostar.wms.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Utils {

	private static final HttpHeaders HEADERS = initHttpHeaders();
	
	private Utils() {
		
	}
	
	private static final HttpHeaders initHttpHeaders() {
		log.info("{}", "Initializing HttpHeaders...");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.ACCEPT_ENCODING, "gzip");
        log.info("HttpHeaders initialized: {}.", headers);
        return headers;
	}
	
	public static final HttpHeaders getHttpHeaders() {
		return HEADERS;
	}
}
