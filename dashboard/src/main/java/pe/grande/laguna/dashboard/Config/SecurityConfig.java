package pe.grande.laguna.dashboard.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import pe.grande.laguna.dashboard.Entity.User;
import pe.grande.laguna.dashboard.Repository.UsersRepository;

import java.io.IOException;
import java.util.Optional;

@Configuration
public class SecurityConfig {

    private final UsersRepository usersRepository;

    public SecurityConfig(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Recursos públicos
                        .requestMatchers(HttpMethod.GET, "/public/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/", "/login").permitAll()
                        // Rutas protegidas por rol
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        // Todas las demás requieren autenticación
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/processlogin")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(authenticationSuccessHandler())
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .logoutSuccessHandler(logoutSuccessHandler())
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            HttpSession session = request.getSession();
            String userEmail = authentication.getName();
            Optional<User> usuario = usersRepository.findByEmail(userEmail);

            usuario.ifPresent(user -> session.setAttribute("user", user));

            // Redirigir según el rol
            String redirectUrl = "/micronetworks";
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if ("ADMIN".equals(authority.getAuthority())) {
                    redirectUrl = "/micronetworks";
                    break;
                }
            }

            new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
        };
    }

    @Bean
    public SimpleUrlLogoutSuccessHandler logoutSuccessHandler() {
        return new SimpleUrlLogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
                    throws IOException, ServletException {

                if (authentication != null) {
                    String userEmail = authentication.getName();
                    usersRepository.findByEmail(userEmail).ifPresent(user -> {
                        user.setActive(false);
                        usersRepository.save(user);
                    });
                }

                request.getSession().setAttribute("logoutMessage", "Se cerró sesión exitosamente");
                setDefaultTargetUrl("/login?logout=true");
                super.onLogoutSuccess(request, response, authentication);
            }
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> response.sendRedirect("/access-denied");
    }
}
