package br.edu.utfpr.cp.espjava.crudcidades.cidade;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/* Semana 05 - Criando entidade persistente
 * É uma classe java simples (Pojo) que segue as especificações da JPA:
 * implementar a interface Serializable; possuir atributos não transientes e não finais;
 * possuir métodos de acesso; e um construtor sem argumentos.
 * Cada classe representa uma tabela no BD e os atributos representam os dados que serão armazenados nas tabelas.
 * Sobre o construtor, o java automaticamente cria um sem argumentos, caso nenhum seja definido.
 */

 /* Define que essa classe será uma classe persistente.
  * "name" define o nome da tabela no BD.
 */

@Entity (name = "cidade")
public class CidadeEntity implements Serializable {

/* A anotação "Id" define o atributo id como sendo a chave primária da tabela
 * GeneratedValue define que essa chave primária vai usar a estratégia de auto-incremento do SGBD
 * para gerar a chave.
 */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String estado;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEstado() {
        return estado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
