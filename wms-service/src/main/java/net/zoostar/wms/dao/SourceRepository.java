package net.zoostar.wms.dao;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import net.zoostar.wms.entity.Source;

public interface SourceRepository extends PagingAndSortingRepository<Source, String> {

	Optional<Source> findBySourceCode(String sourceCode);

}
