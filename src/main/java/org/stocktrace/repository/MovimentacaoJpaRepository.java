package org.stocktrace.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.stocktrace.model.Movimentacao;

import java.util.List;

public interface MovimentacaoJpaRepository extends JpaRepository<Movimentacao, Long> {

    List<Movimentacao> findByEstoque_Id(Long estoqueId);


}
