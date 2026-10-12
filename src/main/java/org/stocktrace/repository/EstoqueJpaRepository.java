package org.stocktrace.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.stocktrace.model.Estoque;


import java.util.Optional;

public interface EstoqueJpaRepository extends JpaRepository<Estoque,Long> {

    Optional<Estoque> findByProduto_IdAndLoja_Id(Long produtoId, Long lojaId);

    boolean  existsByProduto_IdAndLoja_Id(Long produtoId, Long lojaId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Estoque e WHERE e.id = :id")
    Optional<Estoque> buscarPorIdParaAtualizacao(@Param("id") Long id);
}
