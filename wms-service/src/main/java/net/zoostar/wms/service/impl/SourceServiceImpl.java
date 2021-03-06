package net.zoostar.wms.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.dao.SourceRepository;
import net.zoostar.wms.entity.Source;
import net.zoostar.wms.service.SourceService;
import net.zoostar.wms.utils.Utils;

@Slf4j
@Service
@Transactional(readOnly = true)
public class SourceServiceImpl<T> implements SourceService<T> {

	public static final String SOURCE_ID_KEY = "sourceId";
	
	@Autowired
	protected SourceRepository sourceRepository;
	
	@Autowired
	protected RestTemplate restTemplate;
	
	@Override
	public Source retrieve(String sourceCode) {
		log.info("Retrieving by source code: {}", sourceCode);
		var entity = sourceRepository.findBySourceCode(sourceCode);
		if(entity.isEmpty()) {
			throw new EntityNotFoundException("No Source found for Code: " + sourceCode);
		}
		return entity.get();
	}

	@Override
	public T retrieve(String sourceCode, String sourceId, Class<T> clazz) {
		Source source = retrieve(sourceCode);
		Map<String, String> request = new HashMap<>(1);
		request.put(SOURCE_ID_KEY, sourceId);
		log.info("Retrieving from source: {}...", source);
		ResponseEntity<T> response = restTemplate.exchange(source.getBaseUrl(), HttpMethod.POST,
				new HttpEntity<>(request, Utils.getHttpHeaders()), clazz);
		if(HttpStatus.OK != response.getStatusCode() || response.getBody() == null) {
			log.warn("Error encountered while fetching entity from source: {}", source);
			log.warn("Response code: {}", response.getStatusCode());
			throw new EntityNotFoundException("Error encountered while fetching entity from source: " + sourceCode);
		}
		return response.getBody();
	}

}
