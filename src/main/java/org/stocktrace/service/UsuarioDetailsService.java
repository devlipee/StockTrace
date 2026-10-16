package org.stocktrace.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.stocktrace.model.Usuario;
import org.stocktrace.repository.UsuarioJpaRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioJpaRepository usuarioJpaRepository;

    public UsuarioDetailsService(
            UsuarioJpaRepository usuarioJpaRepository
    ) {
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioJpaRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuário não encontrado.")
                );

        return User.withUsername(usuario.getEmail())
                .password(usuario.getSenhaHash())
                .authorities(java.util.Collections.emptyList())
                .disabled(!usuario.isAtivo())
                .build();

    }


}