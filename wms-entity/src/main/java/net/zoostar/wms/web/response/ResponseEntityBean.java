package net.zoostar.wms.web.response;

import org.springframework.http.ResponseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ResponseEntityBean<T> {
	
	private ResponseEntity<T> response;

	public ResponseEntityBean(ResponseEntity<T> response) {
		this.response = response;
	}
	
}
