package com.example.whiskervet.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/h2/*").permitAll()
                
                .requestMatchers("/dashboard/**").hasAuthority("ADMIN")
                                
                .requestMatchers("/veterinario/details").hasAnyAuthority("VETERINARIO")
                .requestMatchers("/veterinario/logout").hasAnyAuthority("VETERINARIO")

                .requestMatchers("/veterinario/all").hasAnyAuthority("ADMIN")
                .requestMatchers("/veterinario/add").hasAnyAuthority("ADMIN")
                .requestMatchers("/veterinario/delete/**").hasAnyAuthority("ADMIN")
                .requestMatchers("/veterinario/update/**").hasAnyAuthority("ADMIN")
                .requestMatchers("/veterinario/activate/**").hasAnyAuthority("ADMIN")
                .requestMatchers("/veterinario/filtrar/**").hasAnyAuthority("ADMIN")
                .requestMatchers("/veterinario/verificar-permisos/add").hasAnyAuthority("ADMIN")
                .requestMatchers("/veterinario/find/**").hasAnyAuthority("ADMIN")

                .requestMatchers("/medicamento/**").hasAnyAuthority("VETERINARIO", "ADMIN")
                .requestMatchers("/tratamiento/all").hasAnyAuthority("VETERINARIO","ADMIN")
                .requestMatchers("/usuario/all").hasAnyAuthority("VETERINARIO","ADMIN")
                .requestMatchers("/mascota/all").hasAnyAuthority("VETERINARIO","ADMIN")
                .requestMatchers("/usuario/find/**").hasAnyAuthority("VETERINARIO","ADMIN")
                .requestMatchers("/tratamiento/verificar-permisos/add").hasAnyAuthority("VETERINARIO","ADMIN")
                .requestMatchers("/usuario/verificar-permisos/add").hasAnyAuthority("VETERINARIO","ADMIN")
                .requestMatchers("/mascota/verificar-permisos/add").hasAnyAuthority("VETERINARIO","ADMIN")

                .requestMatchers("usuario/mascotas/**").hasAnyAuthority("CLIENTE", "ADMIN", "VETERINARIO")
                .requestMatchers("user/roles/**").permitAll()

                
                .requestMatchers("/usuario/details").hasAuthority("CLIENTE")
                
                .anyRequest().permitAll()
            )
            .exceptionHandling( exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint));
            http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
         return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

        /*
     * Permite autenticar a los usuarios con usuario y contrasena
     * Al autenticar devuelve un onjeto Authentication que posteriormente se puede usar a traves de SecurityContextHolder
     * para obtener el usuario autenticado
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }
}

