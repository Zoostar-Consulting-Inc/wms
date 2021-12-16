package net.zoostar.wms.web.response;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GZIPClientHttpResponseWrapper implements ClientHttpResponse {

    public static final String CONTENT_ENCODING = "Content-Encoding";
    
    public static final String GZIP = "gzip";

	private final ClientHttpResponse response;
	
	public GZIPClientHttpResponseWrapper(ClientHttpResponse response) {
		this.response = response;
	}
	
	@Override
	public InputStream getBody() throws IOException {
		InputStream is = null;
		var value = response.getHeaders().getFirst(CONTENT_ENCODING);
		if(GZIP.equalsIgnoreCase(value)) {
			log.info("Received {}: {}", CONTENT_ENCODING, value);
			is = new GZIPInputStream(response.getBody());
		} else {
			is = response.getBody();
		}
		return is;
	}

	@Override
	public HttpHeaders getHeaders() {
		return response.getHeaders();
	}

	@Override
	public HttpStatus getStatusCode() throws IOException {
		return response.getStatusCode();
	}

	@Override
	public int getRawStatusCode() throws IOException {
		return response.getRawStatusCode();
	}

	@Override
	public String getStatusText() throws IOException {
		return response.getStatusText();
	}

	@Override
	public void close() {
		response.close();
	}

}
