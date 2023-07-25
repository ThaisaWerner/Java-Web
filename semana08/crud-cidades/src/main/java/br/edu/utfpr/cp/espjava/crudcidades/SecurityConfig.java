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
 * 
 * Listeners, que permitem empregar o padrão publisher-subscriber (observer) de uma forma muito simples com o Spring Boot.
 * Eles são parte do gerenciamento de eventos do Spring (https://docs.spring.io/spring-framework/reference/). Um evento é algo que acontece,
 * como o login na aplicação, o acesso ao BD, ou a inicialização da aplicação. Esses eventos emitem notificações que são captadas por listeners.
 * Assim como quase tudo no Spring, eventos e listeners podem ser estendidos para atender necessidades específicas. Nessa semana, vamos ver
 * como usar um evento e um listener para identificar a usuária atualmente logada na aplicação.
 * 
 * Sessões e cookies, que permitem armazenar dados temporariamente. Sessões guarda dados em um espaço na memória do servidor e
 * cookies guarda dados diretamente no navegador (explicando mais sobre cookies no CidadeController)
 * 
 * LISTENER: executa um método quando um evento é disparado. Ex: evento: login -> executa método x
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {

/* Como a nossa aplicação está usando autenticação e autorização, precisamos adicionar uma regra permitindo acesso a URL /mostrar */
    @Bean
    protected SecurityFilterChain filter(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/").hasAnyRole("listar", "admin")
                .requestMatchers("/criar", "/excluir", "/alterar", "/preparaAlterar").hasRole("admin")
                .requestMatchers("/mostrar").authenticated() //autorizando novo método do CidadeController (/mostrar)
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

/* Registrando o login
 * Um listener atua sob um método que você definiu. Então começamos criando esse metodo, nessa classe, porque ele está relacionado
 * com a autenticação, mas poderia ter sido criado em qualquer classe gerenciada pelo Spring Boot.
 * O objeto InteractiveAutentica... carrega valores e métodos que são entregues pelo evento que foi disparado pelo listener.
 * Podemos usar esse objeto para extrair o nome da usuária atualmente logada.
 * O Spring Boot se encarrega de instanciar o objeto por meio da injeção de dependências (pesquisar + sobre).
 * A anotação abaixo identifica que o método deve ser acionado por um listener. A classe passada como argumento representa um evento
 * que é disparado pelo Spring Boot quando a usuária loga na aplicação. A anotação serve como um inscrito, que ouve esse tipo de evento.
 * O próprio framework cuida de tudo e, quando o evento é disparado, o método que definimos entra em ação imprimindo o nome da usuária no console.
 */
    @EventListener(InteractiveAuthenticationSuccessEvent.class)
    public void printUsuariaAtual(InteractiveAuthenticationSuccessEvent event) {

    /* O método getAuthentication retorna o objeto que representa a autenticação e por meio dele podemos ter acesso ao método getName
     * que retorna o nome da usuaria atual. Após ter identificado a usuária, vamos imprimir seu nome no console.
     */
        var usuaria = event.getAuthentication().getName();

        System.out.println(usuaria);
    }
}