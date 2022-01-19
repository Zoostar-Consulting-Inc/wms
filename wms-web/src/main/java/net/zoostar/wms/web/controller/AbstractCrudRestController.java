package net.zoostar.wms.web.controller;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.entity.AbstractMultiSourceStringPersistable;
import net.zoostar.wms.entity.EntityWrapper;
import net.zoostar.wms.service.SourceService;
import net.zoostar.wms.service.StringPersistableCrudService;

@Slf4j
public abstract class AbstractCrudRestController<E extends EntityWrapper<T>, T extends AbstractMultiSourceStringPersistable> {
	
	@Autowired
	protected SourceService<E, T> sourceManager;

	protected abstract Class<E> getEntityWrapperClazz();
	
	protected abstract StringPersistableCrudService<T> getCrudManager();

	protected abstract E toEntityWrapper(String sourceCode, String sourceId);
	
	protected ResponseEntity<T> create(EntityWrapper<T> entity) {
		return new ResponseEntity<>(getCrudManager().create(entity.toEntity()), HttpStatus.CREATED);
	}

	protected T retrieveByKey(E entity) {
		return getCrudManager().retrieveByKey(entity.toEntity());
	}

	protected ResponseEntity<T> update(String sourceCode, String sourceId) {
		ResponseEntity<T> response = null;
		E persistable = toEntityWrapper(sourceCode, sourceId);
		T entity = persistable.toEntity();
		log.info("Evaluating CRUD operation for entity: {}...", persistable);
		try {
			entity = getCrudManager().retrieveByKey(entity);
			response = update(entity);
		} catch(EntityNotFoundException e) {
			log.info("No existing entity found, proceeding to CREATE new one...");
			var entityResponse = sourceManager.retrieve(sourceCode, sourceId, getEntityWrapperClazz());
			if(entityResponse == null || HttpStatus.OK != entityResponse.getStatusCode()) {
				log.warn("Source system returned response: {}", entityResponse.getStatusCode());
				response = new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
			} else {
				persistable = entityResponse.getBody();
				log.info("Creating new entity from source: {}", persistable);
				entity = getCrudManager().create(persistable.toEntity());
				response = new ResponseEntity<>(entity, HttpStatus.CREATED);
			}
		}
		postUpdateListener(persistable);
		return response;
	}

	protected ResponseEntity<T> update(T entity) {
		ResponseEntity<T> response = null;
		try {
			var sourceResponse = sourceManager.retrieve(
					entity.getSourceCode(), entity.getSourceId(), getEntityWrapperClazz());
			if(sourceResponse == null || HttpStatus.OK != sourceResponse.getStatusCode()) {
				response = new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
			} else {
				var persistable = sourceResponse.getBody();
				log.info("Updating existing entity from source: {}", persistable);
				entity = getCrudManager().retrieveByKey(persistable.toEntity());
				var sourceEntity = persistable.toEntity();
				sourceEntity.setId(entity.getId());
				entity = getCrudManager().update(sourceEntity);
				response = new ResponseEntity<>(entity, HttpStatus.OK);
			}
		} catch(EntityNotFoundException e) {
			log.info("No entity found in source. Proceeding with deletion: {}", entity);
			entity = delete(entity);
			response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return response;
	}

	protected T delete(T entity) {
		return getCrudManager().delete(entity.getId());
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<T> handleExceptions(EntityNotFoundException e) {
		log.warn(e.getMessage());
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	protected void postUpdateListener(E persistable) {
		// To be implemented, if needed, by extending classes
	}

}
