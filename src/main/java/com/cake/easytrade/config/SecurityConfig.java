package com.cake.easytrade.config;

import com.cake.easytrade.core.Const;
import com.cake.easytrade.model.Role;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_URL = new String[]{
            "/favicon*",
            "/login", Const.API+"/login",
            Const.API+"/common/user/change/password",
            Const.API+Const.PUBLIC_URL+"/**", //항상 open 되는 url
            Const.API+"/user/register", Const.API+"/user/logout",
            "/error", "/api/error",
            "/api-docs/**", "/swagger-ui/**", "/v3/**", "/webjars/**" //swagger
    };
    private static final String[] ADMIN_URL = new String[]{
            Const.API+Const.SYSTEM_URL+"/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .headers((headerConfig) -> headerConfig.frameOptions((HeadersConfigurer.FrameOptionsConfig::disable)))
                .authorizeHttpRequests((authorizeRequests) -> {
                    authorizeRequests.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll();
                    authorizeRequests.requestMatchers(PUBLIC_URL).permitAll();
                    authorizeRequests.requestMatchers(ADMIN_URL).hasAnyAuthority("ROLE_ADMIN");
                    authorizeRequests.anyRequest().authenticated();
                })
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionFixation().migrateSession()
                )
                .logout((logoutConfig ->
                                logoutConfig.logoutUrl(Const.API+"/logout")
                                        .invalidateHttpSession(true)  // 세션 무효화
                                        .deleteCookies("JSESSIONID")  // 쿠키 삭제
                                        .permitAll()
                        )
                );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

