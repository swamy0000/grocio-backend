package com.grocio.backend.security;

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

        private final JwtAuthFilter jwtAuthFilter;

        public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
                this.jwtAuthFilter = jwtAuthFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(cors -> {
                                }) // CORS అలో చేస్తుంది
                                .authorizeHttpRequests(auth -> auth
                                                // 🟢 కేవలం పబ్లిక్ గా ఉండాల్సిన వాటికే ఇక్కడ పర్మిషన్ ఇస్తున్నాం
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                                .requestMatchers(
                                                                "/api/auth/**",
                                                                "/api/home/**",
                                                                "/api/categories/**",
                                                                "/api/products/**",
                                                                "/api/subcategories/**",
                                                                "/api/banners/**",
                                                                "/ws/**")
                                                .permitAll()

                                                // 🔒 పైన లిస్ట్లో లేనివి అన్నీ ఆటోమేటిక్ గా లాగిన్ (JWT Token) ఉంటేనే
                                                // లోపలికి వస్తాయి!
                                                .anyRequest().authenticated())

                                // సెషన్స్ వద్దు, ఎందుకంటే మనం JWT వాడుతున్నాం కాబట్టి
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                // మన కస్టమ్ JwtFilter ని ముందు పెడుతున్నాం
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}