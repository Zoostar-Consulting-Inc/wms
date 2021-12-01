package net.zoostar.wms.web.controller;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractCommonErrorHandler<T> {
	
	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<T> handleExceptions(EntityNotFoundException e) {
		log.warn(e.getMessage());
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

}
