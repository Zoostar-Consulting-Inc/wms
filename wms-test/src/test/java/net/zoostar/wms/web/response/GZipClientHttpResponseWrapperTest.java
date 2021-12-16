package net.zoostar.wms.web.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.web.interceptor.GZIPClientHttpRequestInterceptor;

@Slf4j
@ExtendWith(SpringExtension.class)
class GZipClientHttpResponseWrapperTest {
	
	@Value("${repositories.path:data}")
	String path;

	@Test
	void testGetUnzippedBody() throws IOException {
		ClientHttpRequestInterceptor interceptor = new GZIPClientHttpRequestInterceptor();
		String expected = "Hello World";
		byte[] body = expected.getBytes();
		
		ClientHttpRequestExecution execution = new ClientHttpRequestExecution() {
			
			@Override
			public ClientHttpResponse execute(HttpRequest request, byte[] body) throws IOException {
				return new GZIPClientHttpResponseWrapper(new MockClientHttpResponse(body, HttpStatus.OK));
			}
		
		};
		
		try(ClientHttpResponse response = interceptor.intercept(new MockClientHttpRequest(), body, execution)) {
			var wrapper = new GZIPClientHttpResponseWrapper(response);
			assertNotNull(wrapper);
			assertEquals(HttpStatus.OK, wrapper.getStatusCode());
			assertEquals(HttpStatus.OK.value(), wrapper.getRawStatusCode());
			assertEquals("OK", wrapper.getStatusText());
			try(InputStream is = wrapper.getBody()) {
				assertNotNull(is);
				var actual = new String(is.readAllBytes());
				assertEquals(expected, actual);
			}
		}
	}

	@Test
	void testGetZippedBody() throws IOException {
		File file = File.createTempFile("customer", ".zip");
		try(InputStream is = new ClassPathResource(path + File.separator + "customer.json").getInputStream()) {
			try (OutputStream os = new GZIPOutputStream(new FileOutputStream(file))) {
				os.write(is.readAllBytes());
			}
			log.info("Zipped content written to file: {}", file.getName());
		}
		
		try(InputStream fis = new FileInputStream(file)) {
			ClientHttpResponse response = new MockClientHttpResponse(fis, HttpStatus.OK);
			response.getHeaders().add(GZIPClientHttpResponseWrapper.CONTENT_ENCODING, GZIPClientHttpResponseWrapper.GZIP);
			try(var wrapper = new GZIPClientHttpResponseWrapper(response)) {
				assertEquals(HttpStatus.OK, wrapper.getStatusCode());
				try(InputStream body = wrapper.getBody()) {
					assertNotNull(body);
					String actual = new String(body.readAllBytes());
					assertNotNull(actual);
					assertTrue(actual.length() > 0);
					log.info("Actual: {}", actual);
					log.info("{}", wrapper.getHeaders().get(GZIPClientHttpResponseWrapper.CONTENT_ENCODING));
				}
			}
		}
	}

}
