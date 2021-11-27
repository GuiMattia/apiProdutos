package br.senac.devweb.api.product.categoria;

import com.querydsl.core.types.Predicate;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoriaRepository extends PagingAndSortingRepository<Categoria, Long>,
        QuerydslPredicateExecutor<Categoria> {

    /**
     *
     * @param filter
     * @return
     */
    List<Categoria> findAll(Predicate filter);


    /**
     *
     * @return
     */
    List<Categoria> findAll();
}

