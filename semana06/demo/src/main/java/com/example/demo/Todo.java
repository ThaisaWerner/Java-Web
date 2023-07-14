package com.example.demo;

import org.springframework.data.mongodb.core.mapping.Document;
/* Semana 06 - Vamos usar uma abordagem bottom-up para desenvolver esse novo projeto, começando pela entidade persistente. */

/* Esse arquivo representa uma tarefa no nosso gerenciador de tarefas
 * Ao invés de uma classe criamos um record. Um Java record é uma entidade imutável para a transferência de dados entre objetos.
 * Dessa forma, uma vez atribuídos valores, eles não podem ser alterados. 
 * Outra característica do record é que ele se torna inviável para uso persistente com JPA, mas funciona bem com MongoDB.
 * Para saber mais sobre records: https://dzone.com/articles/spring-boot-and-java-16-records
 * 
 */
/* Criando entidade que representa uma tarefa (todo) */
@Document
/* Diferente de uma classe, um record define os atributos logo na sua declaração */
public record Todo(String id, String title, String description, boolean done) {

    public Todo {
    /* Para garantir que toda tarefa tem um título minimamente descritivo, vamos adicionar uma validação.
     */
        if (title == null || title.length() < 3) {
            throw new IllegalArgumentException("É necessário um título maior do que 3 caracteres.");
        }
    }
    
}

/* O Spring Data reconhece o uso da entidade, mesmo sem nenhuma anotação indicando isso.
 * Mas isso não funciona para entidades JPA.
 */
