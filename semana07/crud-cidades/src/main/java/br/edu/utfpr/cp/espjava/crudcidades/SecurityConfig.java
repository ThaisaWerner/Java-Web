package br.edu.utfpr.cp.espjava.crudcidades;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/* Semana 07 - Configurando a autenticação. Fazendo essas configurações não teremos mais usuário padrão e sim os que criamos. 
 * As anotações abaixo: EnavleWebSecurity: habilita o uso dos recursos do Spring Security
 * e Configuration - indica que essa classe carrega configurações que devem ser usadas pelo spring boot
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {

/* O método abaixo retorna um objeto InMemoryUserDetailsManager que representa como o gerenciamento de usuárias é feito.
   Nesse exemplo, as usuárias estarão armazenadas em memória. Depois armazenaremos no BD (explicação em Usuaria.java)
    @Bean
    public InMemoryUserDetailsManager configure() throws Exception {

        Aqui, cada usuária é definida programaticamente como uma instância de UserDetails. 
        A classe User fornece um método utilitário que permite criar uma nova usuária (withUsername(String)) e papéis associados (roles(String)).
        O método cifrador() é utilizado para atribuir a senha da usuária. Nesse caso, o mesmo cifrador usado aqui é usado também 
        no PasswordEncoder para verificar a senha.
        UserDetails john = User.withUsername("john")
                                .password(cifrador().encode("test123"))
                                .roles("listar")
                                .build();

        UserDetails anna = User.withUsername("anna")
                                .password(cifrador().encode("test123 "))
                                .roles("admin")
                                .build();

        Retornando um novo objeto com as duas usuárias criadas.
        return new InMemoryUderDetailsManager(john, anna);
    } */

/* Configurando autorização
 * É importante destacar que o parâmetro e o tipo de retorno desse método não podem ser alterados. 
 * O mecanismo de segurança do Spring Security é baseado na execução de uma cadeia de filtros.
 * Os filtros são objetos que carregam regras de acesso, definindo as autorizações, por exemplo. O objeto SecurityFilterChain
 * define o método abaixo como mais um filtro que deve ser executado nesse cadeia.
 * Já o objeto HttpSecurity permite acesso à métodos para definir as autorizações dos papéis das usuárias.
 * Mais sobre como funcionam os filtros de segurança: https://docs.spring.io/spring-security/reference/servlet/architecture.html
 * 
 * Aqui, a anotação @Bean permite que o Spring se encarregue de cuidar do ciclo de vida do método e adicioná-lo no momento certo de execução.
 */
    @Bean
    public SecurityFilterChain filter(HttpSecurity http) throws Exception {

    /* Com o método criado, podemos criar as regras de autorização. Aqui começaremos usando o parâmetro http para desabilitar o CSRF (?) ->
     * https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#page-title
     * Depois mapeamos as autorizações com as URLs da aplicação. Para isso estamos usando o método authorizeHttpRequests()
     * seguido das URLs que queremos proteger (requestMatchers para URL) e dos papéis autorizados para cada URL (hasAnyRole para um conjunto de papés)
     * ou (hasRole para um único papel).
     * 
     * Aqui definimos que a URL raiz ("/") que foi mapeada na CidadeController como o método listar(), pode ser acessada por qualquer usuária com
     * os papéis listar ou admin, ou seja, as duas usuárias que criamos.
     * E as outras URLs, apenas admin pode acessar.
     * 
     * Cada URL foi mapeada anteriormente com uma operação CRUD.
     * 
     * Também, para evitar que qualquer URL não definida anteriormente seja deixada aberta para acesso, vamos negar o acesso a qualquer
     * outra URL não definida explicitamente usando anyRequest().denyAll().
     * 
     * Por fim, liberamos o acesso à página de login usando and().formLogin().permitAll(). O método and() é usado como uma conjunção,
     * permitindo adicionar mais um conjunto de regras.
    */
        return http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/").hasAnyRole("listar", "admin")
                .requestMatchers("/criar", "/excluir", "/alterar", "/preparaAlterar").hasRole("admin")
                .anyRequest().denyAll()
                .and()
                .formLogin().permitAll() 
                .loginPage("/login.html").permitAll() /* Permitindo acesso a página de login que criamos */
                .and()
                .logout().permitAll() /* Permitindo acesso a página de logout que criamos */
                .and()
                .build();
    }

/* Criando o código para cifrar as senhas
 * Para garantir que as senhas são armazenadas de forma segura, o Spring Security depende de um algoritimo de cifragem. 
 * Nesse método definimos qual algoritmo iremos usar. O objeto PasswordEncoder defne uma interface que todos os algoritmos de cifragem
 * devem seguir.
 */
    @Bean
    public PasswordEncoder cifrador () {
    /* O Spring já possui alguns cifradores e aqui dizemos qual desses algoritmos de cifragem que será usado */
        return new BCryptPasswordEncoder();
    }

/* Nós estamos usando um algoritmo de cifragem para garantir que as senhas não sejam armazenadas como texto puro, por isso, para
 * criarmos uma usuária no BD, precisamos cifrar a senha antes de salvá-la. Nesse exemplo, vamos criar a usuária direto no BD, mas
 * é importante lembrar disso quando tiver criando uma aplicação real.
 * Então, para isso, criamos o método abaixo. Seu objetivo é usar o algoritmo de cifragem para cifrar a senha de exemplo e impimí-la
 * no console, assim podemos pegar essa senha e salvá-la direto no BD.
 * 
 * Tentar fazer sozinha depois *---* (É necessário cifrar a senha informada pela usuária antes de enviar para o BD, para garantir a
 * segurança da usuária e para que o Spring Security consiga comparar a senha informada na tela de login com a senha cifrada armazenada no BD)
 */
    @EventListener(ApplicationReadyEvent.class)
    public void printSenhas() {
        System.out.println(this.cifrador().encode("teste123"));
    }

    
}
/* Uma autorização ou papel define um grupo de autorizações / restrições que um ou mais usuários possuem. Os usuários e seus papéis / autorizações
 * podem ser armazenados em diferentes locais, como na memória da aplicação ou no banco de dados. No exemplo dessa semana, estamos definindo
 * as usuárias na memória da aplicação.
 */

 /* Papéis e autorizações podem ser usados como sinônimos em alguns materiais. O Spring implementa esses conceitos como duas coisas diferentes
  * Aqui também estaremos usando como sinônimos, mas em configurações de segurança mais complexas, talvez precise-se dos dois
 */