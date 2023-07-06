package com.example.demo;

import org.springframework.data.mongodb.core.mapping.Document;

/* Criando entidade que representa uma tarefa (todo) */
@Document
public record Todo(String id, String title, String description, boolean done) {

    public Todo {
        if (title == null || title.length() < 3) {
            throw new IllegalArgumentException("É necessário um título maior do que 3 caracteres.");
        }
    }
    
}
