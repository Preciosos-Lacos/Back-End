package com.lacos_preciosos.preciososLacos.infra.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfigurations(private val securityFilter: SecurityFilter) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .cors { }
            .authorizeHttpRequests {
                // Rotas públicas
                it.requestMatchers(HttpMethod.GET, "/pedidos").permitAll()
                it.requestMatchers(HttpMethod.GET, "/pedidos/**").permitAll()
                it.requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                it.requestMatchers(HttpMethod.POST, "/usuarios/login").permitAll()
                it.requestMatchers(HttpMethod.GET, "/modelos").permitAll()
                it.requestMatchers(HttpMethod.POST, "/caracteristica-detalhe/corModelo").permitAll()
                it.requestMatchers(HttpMethod.POST, "/caracteristica-detalhe/cor").permitAll()
                it.requestMatchers(HttpMethod.GET, "/caracteristica-detalhe/cor").permitAll()
                it.requestMatchers(HttpMethod.GET, "/caracteristica-detalhe/cor/{id}/completo").permitAll()
                it.requestMatchers(HttpMethod.GET, "/caracteristica-detalhe/cor/{id}").permitAll()
                it.requestMatchers(HttpMethod.PATCH, "/caracteristica-detalhe/cor/{id}").permitAll()
                it.requestMatchers(HttpMethod.DELETE, "/caracteristica-detalhe/cor/{id}").permitAll()
                it.requestMatchers(HttpMethod.GET, "/api/pedidos").permitAll()
                it.requestMatchers(HttpMethod.GET, "/api/pedidos/").permitAll()
                it.requestMatchers(HttpMethod.GET, "/api/pedidos/{id}").permitAll()
                it.requestMatchers(HttpMethod.GET, "/api/pedidos/search").permitAll()
                it.requestMatchers(HttpMethod.GET, "/dashboard").permitAll()
                it.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/produtos").permitAll()
                it.requestMatchers(HttpMethod.GET, "/produtos/promocoes").permitAll()
                it.requestMatchers(HttpMethod.GET, "/produtos/destaques").permitAll()
                // Banners
                it.requestMatchers(HttpMethod.GET, "/banners").permitAll()
                it.requestMatchers(HttpMethod.GET, "/banners/{id}").permitAll()
                it.requestMatchers(HttpMethod.GET, "/banners/ativo/home").permitAll()
                it.requestMatchers(HttpMethod.POST, "/banners/upload").permitAll()
                it.requestMatchers(HttpMethod.POST, "/banners").permitAll()
                it.requestMatchers(HttpMethod.PUT, "/banners/{id}").authenticated()
                it.requestMatchers(HttpMethod.DELETE, "/banners/{id}").authenticated()
                it.requestMatchers(HttpMethod.PATCH, "/banners/{id}/ativo").permitAll()
                // Todas as outras precisam de autenticação
                it.anyRequest().permitAll()
            }
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager {
        return configuration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
