package com.example.demo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

/* Criando interface responsável pela persistência */
@Repository
public interface TodoRepository 
    extends ReactiveMongoRepository<Todo, String> {

        Flux<Todo> findByDone(boolean done);
    
}
