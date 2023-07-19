package br.edu.utfpr.cp.espjava.crudcidades;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/* Semana 08 - Tratando sobre 3 tópicos complementares no contexto dessa aplicação que estamos usando como exemplo, mas importantes para
 * o desenvolvimento de aplicaações web num geral. São eles: 
 * Listeners, que permitem empregar o padrão publisher-subscriber (observer) de uma forma muito simples com o Spring Boot.
 * Sessões e cookies, que permitem armazenar dados temporariamente. Sessões guarda dados em um espaço na memória do servidor e
 * cookies guarda dados diretamente no navegador.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain filter(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/").hasAnyRole("listar", "admin")
                .requestMatchers("/criar", "/excluir", "/alterar", "/preparaAlterar").hasRole("admin")
                .requestMatchers("/mostrar").authenticated() //autorizando novo método do CidadeController
                .anyRequest().denyAll()
                .and()
                .formLogin()
                .loginPage("/login.html").permitAll()
                .and()
                .logout().permitAll()
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder cifrador () {
        return new BCryptPasswordEncoder();
    }

/* Registrando o login */
    @EventListener(InteractiveAuthenticationSuccessEvent.class)
    public void printUsuariaAtual(InteractiveAuthenticationSuccessEvent event) {

        var usuaria = event.getAuthentication().getName();

        System.out.println(usuaria);
    }
}