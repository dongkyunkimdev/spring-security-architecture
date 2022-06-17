package com.dkkim.springsecurity.security;

import com.dkkim.springsecurity.security.handler.CustomAccessDeniedHandler;
import com.dkkim.springsecurity.security.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// WebSecurityConfigurerAdapter가 deprecated되어, Lambda DSL로 구성
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private static final String[] AUTH_EXCLUDE_LIST = {"/api/health-check"};

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Token을 사용하는 Rest API 서버에서는 csrf를 사용하지 않음
                .csrf().disable()
                // form login 사용하지 않음
                .httpBasic().disable()
                .logout().disable()

                // 세션을 사용하지 않음
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // cors config 등록
                .cors().configurationSource(corsConfigurationSource())
                .and()

                // 인증 없이 허용할 url
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(AUTH_EXCLUDE_LIST).permitAll()
                .anyRequest().authenticated()
                .and()

                // Custom Handler 등록
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
        ;

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
