package org.stocktrace.config;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.stocktrace.service.UsuarioDetailsService;

@Configuration
public class SecurityConfig {

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            UsuarioDetailsService usuarioDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(usuarioDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DaoAuthenticationProvider authenticationProvider
    ) throws Exception {

        http
                // Usa os usuários do banco e o encoder configurado.
                .authenticationProvider(authenticationProvider)

                // Mantém a proteção CSRF habilitada.
                .csrf(Customizer.withDefaults())

                // Cria uma sessão quando necessário.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                // Define quais requisições exigem autenticação.
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/csrf").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .anyRequest().authenticated()
                )

                // Login processado pelo filtro do Spring Security.
                .formLogin(form -> form
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("senha")
                        .successHandler((request, response, authentication) ->
                                response.setStatus(HttpServletResponse.SC_OK)
                        )
                        .failureHandler((request, response, exception) ->
                                response.setStatus(
                                        HttpServletResponse.SC_UNAUTHORIZED
                                )
                        )
                )

                // Encerra a sessão no logout.
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler((request, response, authentication) ->
                                response.setStatus(
                                        HttpServletResponse.SC_NO_CONTENT
                                )
                        )
                )

                // Retorna status HTTP, sem redirecionar para páginas.
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint((request, response, exception) ->
                                response.setStatus(
                                        HttpServletResponse.SC_UNAUTHORIZED
                                )
                        )
                        .accessDeniedHandler((request, response, exception) ->
                                response.setStatus(
                                        HttpServletResponse.SC_FORBIDDEN
                                )
                        )
                )

                // A API não precisa guardar URLs para redirecionar após login.
                .requestCache(AbstractHttpConfigurer::disable)

                // A autenticação será por sessão, sem HTTP Basic.
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}