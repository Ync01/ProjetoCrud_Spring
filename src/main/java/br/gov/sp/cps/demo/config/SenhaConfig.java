package br.gov.sp.cps.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SenhaConfig {

    // BCrypt transforma a senha em um hash (texto embaralhado que não volta ao original).
    // Cada hash tem um "sal" aleatório, então a mesma senha gera hashes diferentes.
    // Para conferir, usamos matches(), que não precisa "desfazer" o hash.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
