package net.zoostar.wms.web.controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractCommonErrorHandler<T> {
	
	@ExceptionHandler(NoSuchElementException.class)
	protected ResponseEntity<T> handleExceptions(NoSuchElementException e) {
		log.warn(e.getMessage());
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

}
