package org.stocktrace.repository;

import jakarta.persistence.LockModeType;
import org.hibernate.internal.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.stocktrace.model.Estoque;
import org.stocktrace.model.Movimentacao;

import java.util.List;

public interface MovimentacaoJpaRepository extends JpaRepository<Movimentacao, Long> {

    List<Movimentacao> findByEstoque_Id(Long estoqueId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Estoque e where e.id = :id")
    Optional<Estoque> buscarPorIdParaAtualizacao(@Param("id") Long id);
}
