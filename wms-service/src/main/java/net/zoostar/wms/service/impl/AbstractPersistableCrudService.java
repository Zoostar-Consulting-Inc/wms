package net.zoostar.wms.service.impl;

import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.zoostar.wms.entity.AbstractMultiSourceStringPersistable;
import net.zoostar.wms.service.StringPersistableCrudService;

@Slf4j
@Getter
@Setter
@Service
@Transactional
public abstract class AbstractPersistableCrudService<T extends AbstractMultiSourceStringPersistable>
implements StringPersistableCrudService<T> {

	protected abstract PagingAndSortingRepository<T, String> getRepository();

	@Override
	public T create(T persistable) {
		if(!persistable.isNew()) {
			throw new EntityExistsException(String.format("Entity exists: %s", persistable.toString()));
		}
			
		log.info("Persisting {}...", persistable.toString());
		persistable.setActive(true);
		persistable.setUpdate(System.currentTimeMillis());
		return getRepository().save(persistable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<T> retrieve(int page, int size) {
		log.info("Retrieving {} for page {}...", size, page);
		Pageable pageable = PageRequest.of(page, size);
		return getRepository().findAll(pageable);
	}

	@Override
	public T update(T persistable) {
		if(persistable.isNew() || getRepository().findById(persistable.getId()).isEmpty()) {
			throw new EntityNotFoundException(String.format(
					"No entity found to update for: %s", persistable.toString()));
		} else {
			persistable.setUpdate(System.currentTimeMillis());
			return getRepository().save(persistable);
		}
	}

	@Override
	public T delete(String id) {
		T entity = null;
		Optional<T> optional = getRepository().findById(id);
		if(optional.isEmpty()) {
			throw new EntityNotFoundException(String.format(
					"No entity found to delete by id: %s", id));
		} else {
			entity = optional.get();
			entity.setActive(false);
			entity.setUpdate(System.currentTimeMillis());
			entity = getRepository().save(entity);
		}
		return entity;
	}

}
