package br.edu.utfpr.cp.espjava.crudcidades.cidade;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/* Semana 05 - criando repositório
 * Classe que implementa as operações de persistência e executa essas operações usando o driver do SGBD.
 * O Spring Data facilita esse processo fornecendo uma interface que já define as operações mais comuns,
 * como as do CRUD. Durante a compilação, o próprio spring data se encarrega de gerar uma implementação
 * da interface que usa as opereções sobre as entidades definidas. Por isso, estamos criando aqui,
 * uma interface que extende essa outra interface, definindo qual a entidade que ela vai gerencial e qual
 * o tipo de dados da chave primária "CidadeEn..., id..."
 * Criando método de busca por nome e estado
*/

public interface CidadeRepository extends JpaRepository<CidadeEntity, Long> {

/*Adicionando método que permite localizar a cidade por nome e estado. Para isso, podemos usar as especificações padrão do Spring Data (https://docs.spring.io/spring-data/jpa/docs/2.4.7/reference/html/#jpa.query-methods.query-creation) 
 * Esse método retorna uma CidadeEntity e recebe dois parâmetros que representam nome e estados informados pela usuária.
 * Como o Spring Data implementa certas coisas para nós, não precisamos fazer o "corpo" do método.
 * Usamos Optional porque não sabemos se a cidade existe. Ele nos permite fazer essas validações antes da exclusão.
*/
    public Optional<CidadeEntity> findByNomeAndEstado(String nome, String estado);
    
}
