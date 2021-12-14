package net.zoostar.wms.web.controller;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.entity.AbstractMultiSourceStringPersistable;
import net.zoostar.wms.service.SourceService;
import net.zoostar.wms.service.StringPersistableCrudService;

@Slf4j
public abstract class AbstractCrudRestController<T extends AbstractMultiSourceStringPersistable> {
	
	@Autowired
	protected SourceService<T> sourceManager;

	protected abstract Class<T> getClazz();
	
	protected abstract StringPersistableCrudService<T> getCrudManager();

	protected abstract T getPersistable(String sourceCode, String sourceId);

	protected T create(String sourceCode, String sourceId) {
		return getCrudManager().create(
				sourceManager.retrieve(sourceCode, sourceId, getClazz()));
	}
	
	protected T retrieveByKey(T entity) {
		return getCrudManager().retrieveByKey(entity);
	}

	protected ResponseEntity<T> update(String sourceCode, String sourceId) {
		T entity = null;
		ResponseEntity<T> response = null;

		try {
			entity = getCrudManager().retrieveByKey(getPersistable(sourceCode, sourceId));
			response = new ResponseEntity<>(update(entity), HttpStatus.OK);
		} catch(EntityNotFoundException e) {
			log.info(e.getMessage());
			entity = create(sourceCode, sourceId);
			response = new ResponseEntity<>(entity, HttpStatus.CREATED);
		}
		postUpdateListener(entity);
		return response;
	}

	protected T update(T entity) {
		T persistable = null;
		try {
			persistable = sourceManager.retrieve(
					entity.getSourceCode(), entity.getSourceId(), getClazz());
			persistable.setId(entity.getId());
			persistable = getCrudManager().update(persistable);
		} catch(EntityNotFoundException e) {
			log.info(e.getMessage());
			persistable = delete(entity);
		}
		return persistable;
	}

	protected T delete(T entity) {
		return getCrudManager().delete(entity.getId());
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<T> handleExceptions(EntityNotFoundException e) {
		log.warn(e.getMessage());
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	protected void postUpdateListener(T entity) {
		// To be implemented, if needed, by extending classes
	}

}
