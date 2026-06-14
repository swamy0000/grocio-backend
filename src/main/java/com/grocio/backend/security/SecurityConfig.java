package com.grocio.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private JwtAuthFilter jwtAuthFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(cors -> {
                                }) // CORS అలో చేస్తుంది
                                .authorizeHttpRequests(auth -> auth
                                                // ఈ API లకి టోకెన్ అవసరం లేదు (పబ్లిక్)
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                                .requestMatchers(
                                                                "/api/users/login",
                                                                "/api/users/register",
                                                                "/api/users/check-phone",
                                                                "/api/users/update-pin",
                                                                "/api/home/**",
                                                                "/api/categories/**",
                                                                "/api/products/**",
                                                                "/api/subcategories/**",
                                                                "/api/users/**",
                                                                "/api/orders/**",
                                                                "/api/addresses/**",
                                                                "/api/cart/**",
                                                                "/api/banners/**",
                                                                "/ws/**")
                                                .permitAll()
                                                // మిగతా ఏ API (Update Profile, Add to Cart etc.) వాడాలన్నా కచ్చితంగా
                                                // లాగిన్
                                                // (టోకెన్) అయి ఉండాలి
                                                .anyRequest().authenticated())
                                // సెషన్స్ వద్దు, ఎందుకంటే మనం JWT వాడుతున్నాం కాబట్టి
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                // మన కస్టమ్ JwtFilter ని ముందు పెడుతున్నాం
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}