package br.edu.utfpr.cp.espjava.crudcidades;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/* Semana 07 - Configurando a autenticação. Fazendo essas configurações não teremos mais usuário padrão e sim os que criamos */
@EnableWebSecurity
@Configuration
public class SecurityConfig {

/* Configurando autorização */
    @Bean
    public SecurityFilterChain filter(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/").hasAnyRole("listar", "admin")
                .requestMatchers("/criar", "/excluir", "/alterar", "/preparaAlterar").hasRole("admin")
                .anyRequest().denyAll()
                .and()
                .formLogin().permitAll() 
                .loginPage("/login.html").permitAll() /* permitindo acesso a página de login/logout */
                .and()
                .logout().permitAll() /* permitindo acesso a página de login/logout */
                .and()
                .build();
    }

/* Criando o código para cifrar as senhas */
    @Bean
    public PasswordEncoder cifrador () {
        return new BCryptPasswordEncoder();
    }
    
}
