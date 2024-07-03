package com.electronic.store.ElectronicStore.config;
import com.electronic.store.ElectronicStore.JWT.JwtAuthentication;
import com.electronic.store.ElectronicStore.JWT.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig
{
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JwtAuthentication startingPoint;
    //we need security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception
    {
        //this is for cors part
        security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());
        //this is for csrf part
        security.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        //Here we are configuring url
        security.authorizeHttpRequests(request-> {
            request.requestMatchers(HttpMethod.POST,"/users/").permitAll().
                   requestMatchers(HttpMethod.GET,"/products").permitAll().
                    requestMatchers("/products/**").hasRole("ADMIN").
                    requestMatchers(HttpMethod.GET,"/category/**").permitAll().
                    requestMatchers("/category/**").hasRole("ADMIN").
                    requestMatchers(HttpMethod.GET,"/orders").hasRole("ADMIN").
                    requestMatchers("/orders/**").hasAnyRole("ADMIN","USER").
                    requestMatchers(HttpMethod.POST,"/auth/generate-token").permitAll().
                    requestMatchers("/auth/**").authenticated()
                    .anyRequest().permitAll();
        });
        //it will activate exception as this is the authentication entry point
        security.exceptionHandling(ex->ex.authenticationEntryPoint(startingPoint));
        security.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        security.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return security.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }


}
