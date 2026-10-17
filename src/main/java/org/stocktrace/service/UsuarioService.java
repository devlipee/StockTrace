package org.stocktrace.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.stocktrace.dto.UsuarioRespostaDTO;
import org.stocktrace.exception.DadosInvalidosException;

import org.stocktrace.exception.EntidadeNaoEncontradaException;
import org.stocktrace.model.Usuario;
import org.stocktrace.repository.UsuarioJpaRepository;

@Service
public class UsuarioService {

    private final UsuarioJpaRepository usuarioJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioJpaRepository usuarioJpaRepository, PasswordEncoder passwordEncoder) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioRespostaDTO cadastrarUsuario(String nome, String email, String senha){

        validarDados(nome, email, senha);

        //valida se o email já foi cadastrado
        if (usuarioJpaRepository.existsByEmail(email)){
            throw new  DadosInvalidosException("O email informado já foi cadastrado.");
        }
        //transforma senha em um hash e armazena na variavel
        String senhaHash = passwordEncoder.encode(senha);

        //cria usuario com a senha hash
        Usuario usuario = new Usuario(
                nome,
                email,
                senhaHash);

        //armazena o usuario salvo na variavel
        Usuario usuarioSalvo = usuarioJpaRepository.save(usuario);

        //retorna usando a classe dto
        return new UsuarioRespostaDTO(
                usuarioSalvo.getId(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getEmail(),
                usuarioSalvo.isAtivo()
        );
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioJpaRepository.findByEmail(email)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Usuário não encontrado."
                ));
    }


    private void validarDados(String nome, String email, String senha) {
        if (nome == null || nome.isBlank()) {
            throw new DadosInvalidosException("O nome é obrigatório.");
        }

        if (nome.length() > 150) {
            throw new DadosInvalidosException(
                    "O nome deve ter no máximo 150 caracteres."
            );
        }

        if (email == null || email.isBlank()) {
            throw new DadosInvalidosException("O e-mail é obrigatório.");
        }

        if (email.length() > 254) {
            throw new DadosInvalidosException(
                    "O e-mail deve ter no máximo 254 caracteres."
            );
        }

        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new DadosInvalidosException("O e-mail informado é inválido.");
        }

        if (senha == null || senha.isBlank()) {
            throw new DadosInvalidosException("A senha é obrigatória.");
        }
    }

}
