package top.lemna.data.jpa.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;


@NoRepositoryBean
public interface  BaseRepository<T, ID> extends PagingAndSortingRepository<T, ID>,JpaSpecificationExecutor<T> {

}