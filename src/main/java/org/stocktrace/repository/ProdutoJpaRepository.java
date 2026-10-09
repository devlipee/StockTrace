package org.stocktrace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stocktrace.model.Produto;

import java.util.Optional;

public interface ProdutoJpaRepository extends JpaRepository<Produto,Long > {

    //busca produto por codigo
    Optional<Produto> findByCodigo(String codigo);

    //verfica se o produto existe e retorna true ou false
    Boolean existsByCodigo(String codigo) ;
}
