package net.zoostar.wms.web.response;

import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.client.MockClientHttpResponse;

import lombok.Getter;

@Getter
public class MockGZIPClientHttpResponse extends MockClientHttpResponse {

	private final HttpHeaders headers;
	
	/**
	 * Constructor with response body as a byte array.
	 */
	public MockGZIPClientHttpResponse(byte[] body, HttpStatus statusCode, HttpHeaders headers) {
		super(body, statusCode);
		this.headers = headers;
	}

	/**
	 * Constructor with response body as InputStream.
	 */
	public MockGZIPClientHttpResponse(InputStream body, HttpStatus statusCode, HttpHeaders headers) {
		super(body, statusCode);
		this.headers = headers;
	}
	
}
