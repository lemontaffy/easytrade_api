package com.cake.easytrade.config;

import com.cake.easytrade.config.core.JwtAuthenticationFilter;
import com.cake.easytrade.core.Const;
import com.cake.easytrade.model.Role;
import jakarta.servlet.DispatcherType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    private static final String[] PUBLIC_URL = new String[]{
            "/favicon*",
            "/uploads/**",
            Const.API+"/auth/login", Const.API+"/auth/refresh", Const.API+"/auth/status",
            Const.API+"/common/user/change/password",
            Const.API+Const.PUBLIC_URL+"/**", //항상 open 되는 url
            Const.API+"/user/register", Const.API+"/user/logout",
            "/error", "/api/error",
            "/api-docs/**", "/swagger-ui/**", "/v3/**", "/webjars/**" //swagger
    };
    private static final String[] ADMIN_URL = new String[]{
            Const.API+Const.SYSTEM_URL+"/**",
    };

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .headers((headerConfig) -> headerConfig.frameOptions((HeadersConfigurer.FrameOptionsConfig::disable)))
//                .authorizeHttpRequests((authorizeRequests) -> {
//                    authorizeRequests.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll();
//                    authorizeRequests.requestMatchers(PUBLIC_URL).permitAll();
//                    authorizeRequests.requestMatchers(ADMIN_URL).hasAnyAuthority("ROLE_ADMIN");
//                    authorizeRequests.anyRequest().authenticated();
//                })
//                .sessionManagement(sessionManagement -> sessionManagement.sessionFixation().migrateSession())
//                .logout(logoutConfig -> logoutConfig.logoutUrl(Const.API + "/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll());
//        http.cors(cors -> cors.configurationSource(corsConfigurationSource))  // Use custom CORS configuration
//                .oauth2Login(oauth2Login -> {
//                    oauth2Login
//                            .loginPage("/login")  // Specify custom login page URL if needed
//                            .permitAll();
//                });
//        return http.build();
        http.csrf(AbstractHttpConfigurer::disable)
                .headers((headerConfig) -> headerConfig.frameOptions((HeadersConfigurer.FrameOptionsConfig::disable)))
                .authorizeHttpRequests((authorizeRequests) -> {
                    authorizeRequests.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll();
                    authorizeRequests.requestMatchers(PUBLIC_URL).permitAll();
                    authorizeRequests.requestMatchers(ADMIN_URL).hasAnyAuthority("ROLE_ADMIN");
                    authorizeRequests.anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter
                .sessionManagement(sessionManagement -> sessionManagement.sessionFixation().migrateSession())
                .logout(logoutConfig -> logoutConfig.logoutUrl(Const.API + "/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll())
                .cors(cors -> cors.configurationSource(corsConfigurationSource));
//                .oauth2Login(oauth2Login -> oauth2Login.loginPage("/login").permitAll());
//                .debug();  // Enable debugging
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

