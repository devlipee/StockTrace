package org.stocktrace.service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.stocktrace.exception.DadosInvalidosException;
import org.stocktrace.exception.EntidadeNaoEncontradaException;
import org.stocktrace.model.Loja;
import org.stocktrace.repository.LojaJpaRepository;

import java.util.List;

@Service
public class LojaService {


    private final LojaJpaRepository lojaJpaRepository;

    public LojaService(LojaJpaRepository lojaJpaRepository){
        this.lojaJpaRepository = lojaJpaRepository;
    }


    public Loja cadastrarLoja(
            String nome,
            String cidade,
            String bairro,
            String rua,
            String numero,
            String complemento,
            String cep
    ){
        validarDadosLoja(nome,cidade,bairro,rua,numero,cep);

        Loja loja = new Loja(
                nome,
                cidade,
                bairro,
                rua,
                numero,
                complemento,
                cep
        );
        return lojaJpaRepository.save(loja);
    }

    public List<Loja> listarLojas(){
        return lojaJpaRepository.findAll();
    }

    public Loja buscarLojaPorId(Long id){
        validarId(id);
        return lojaJpaRepository.findById(id).orElseThrow(()-> new EntidadeNaoEncontradaException(
                "Loja não encontrada. ID: "+ id
        ));
    }

    @Transactional
    public Loja atualizarLoja(
            Long id,
            String nome,
            String cidade,
            String bairro,
            String rua,
            String numero,
            String complemento,
            String cep
    ) {
        validarDadosLoja(nome,cidade,bairro,rua,numero,cep);

        Loja loja = buscarLojaPorId(id) ;

        loja.atualizarDados(
                nome,
                cidade,
                bairro,
                rua,
                numero,
                complemento,
                cep
        );
        return lojaJpaRepository.save(loja);
    }

    @Transactional
    public void deletarLoja(Long id) {

        Loja loja = buscarLojaPorId(id);
        lojaJpaRepository.delete(loja);
    }

    // Validações privadas reutilizadas pelos métodos deste Service.

    private void validarId(Long id) {

        if (id == null || id <= 0) {
            throw new DadosInvalidosException(
                    "O ID da loja deve ser maior que zero."
            );
        }
    }

    private void validarDadosLoja(
            String nome,
            String cidade,
            String bairro,
            String rua,
            String numero,
            String cep
    ) {
        if (nome == null || nome.isBlank()) {
            throw new DadosInvalidosException("O nome da loja é obrigatório.");
        }

        if (cidade == null || cidade.isBlank()) {
            throw new DadosInvalidosException("A cidade da loja é obrigatória.");
        }

        if (bairro == null || bairro.isBlank()) {
            throw new DadosInvalidosException("O bairro da loja é obrigatório.");
        }

        if (rua == null || rua.isBlank()) {
            throw new DadosInvalidosException("A rua da loja é obrigatória.");
        }

        if (numero == null || numero.isBlank()) {
            throw new DadosInvalidosException("O número da loja é obrigatório.");
        }

        if (cep == null || cep.isBlank()) {
            throw new DadosInvalidosException("O CEP da loja é obrigatório.");
        }

        // Aceita os formatos 01000000 e 01000-000.
        if (!cep.matches("[0-9]{5}-?[0-9]{3}")) {
            throw new DadosInvalidosException(
                    "O CEP deve conter 8 dígitos, no formato 01000000 ou 01000-000."
            );
        }

        // Complemento é opcional.
    }
}