package org.stocktrace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stocktrace.model.Estoque;
import org.stocktrace.model.Produto;

import java.util.Optional;

public interface EstoqueJpaRepository extends JpaRepository<Estoque,Long> {

    Optional<Estoque> findByProduto_IdAndLoja_Id(Long produtoId, Long lojaId);

    boolean  existsByProduto_IdAndLoja_Id(Long produtoId, Long lojaId);
}
