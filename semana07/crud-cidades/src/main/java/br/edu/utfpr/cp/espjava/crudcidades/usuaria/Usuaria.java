package br.edu.utfpr.cp.espjava.crudcidades.usuaria;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/* Semana 07 - Mudando autenticação para o BD

 * Com as usuárias armazenadas na memória, a autenticação funciona bem para uma aplicação pequena com usuárias estáticas, mas normalmente
 * a criação de usuárias é feita de forma dinâmica, sem que ela dependa de ajustes no código para começar a usar o aplicativo.
 * Então vamos ajustar o código para que a autenticação esteja baseada em usuárias em um BD, ao invés de código direto na aplicação.
 * 
 * Continuamos usando a arquitetura definida anteriormente, então foi criada uma pasta para usuárias e aqui uma classe que presenta essa entidade.
 * 
 * Tudo que precisamos fazer para marcar a classe usuária como sendo uma usuária do sistema de forma que o Spring Security entenda, 
 * é implementar a interface UserDetails. Ela define um conjunto de métodos que precisam ser implementados por uma uma usuária do sistema.
 * Eles estão marcados ao longo do código
 */
@Entity
public class Usuaria implements Serializable, UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String senha;

/* Aqui teremos uma relação de muitos para muitos, uma usuária pode ter vários papéis e cada papel pode ser usado por várias usuárias.
 * Mas, ao invés de usar a anotação ManyToMany, usamos a anotação abaixo. Ela cria automaticamente uma relação entre a entidade (Usuária)
 * e a lista (de papéis). Essa solução foi usada, pois não há outras entidades se relacionando com a lista de papéis.
*/
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> papeis;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public List<String> getPapeis() {
        return papeis;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setPapeis(List<String> papeis) {
        this.papeis = papeis;
    }

/* Método que precisa ser implementado por uma usuária do sistema - UserDetails.
 * Retorna uma lista de papéis na qual a usuária tem permissões. Na classe Usuaria, os papéis estão definidos no atributo List papeis,
 * então precisamos implementar esse método retornando o valor armazenado no atributo papeis (return this.papeis), mas não só isso.
 * O atributo papeis é uma lista de String, enquanto o método abaixo retorna uma coleção de GrantedAuthority, então precisamos converter
 * cada papel do tipo String para um SimpleGrantedAuthority (.stream...) 
 */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.papeis
                    .stream()
                    .map(papelAtual -> new SimpleGrantedAuthority("ROLE_" + papelAtual))
                    .collect(Collectors.toList());
    }

/* Método que precisa ser implementado por uma usuária do sistema - UserDetails.
 * Mesma coisa que getUsername, só que para senha
 */
    @Override
    public String getPassword() {
       return this.senha;
    }

/* Método que precisa ser implementado por uma usuária do sistema - UserDetails.
 * Ele retorna o nome da usuária. Como o nome da usuária é definido no atributo nome, tudo que precisamos fazer para implementar
 * esse método é retornar o valor armazenado no atributo nome (return this.nome)
 */
    @Override
    public String getUsername() {
        return this.nome;
    }

/* Para os métodos abaixo, em um cenário real, precisaríamos de atributos adicionais na classe para armazenar esses valores
 * de checagem dos métodos, mas nesse caso de exemplo, vamos sempre retornar o valor fixo true.
 */

/* Método que precisa ser implementado por uma usuária do sistema - UserDetails.
 * Retorna verdadeiro ou falso para se a conta está expirada ou não. 
 */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

/* Método que precisa ser implementado por uma usuária do sistema - UserDetails.
 * Retorna verdadeiro ou falso para se a conta está travada ou não. 
 */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

/* Método que precisa ser implementado por uma usuária do sistema - UserDetails.
 * Retorna verdadeiro ou falso para se as credenciais estão expiradas ou não. 
 */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

/* Método que precisa ser implementado por uma usuária do sistema - UserDetails.
 * Retorna verdadeiro ou falso para se a conta está habilitada ou não.
 */
    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
