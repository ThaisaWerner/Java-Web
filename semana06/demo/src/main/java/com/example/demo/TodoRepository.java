package com.example.demo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

/* Semana 06 - Criar a parte de persistência com o WebFlux não é diferente do que já fizemos com o MVC,
 * pois o Spring Data oferece uma interface para repositórios reativos (Reactive CrudRepository) e
 * tanto a interface para repositórios reativos e não reativos é baseada na mesma interface (Repository),
 * dessa forma a implementação é exatamente a mesma.
 */

/* Criando interface responsável pela persistência
 * Ela é responsável pelas operações de gerenciamento dos dados de tarefas que é representado pelo Java record Todo.
 */
@Repository //Anotação identificando a interface como responsável por gerenciar os dados da aplicação
public interface TodoRepository 
/* Herda comportamentos da interface ReactiveMongoRepository. Esses comportamentos correspondem às operações básicas de um CRUD,
 * como ler e salvar. Essa interface (ReactiveMongoRepository) é uma especialização de outra interface (ReactiveCrudRepository) para o MongoDB
 */
    extends ReactiveMongoRepository<Todo, String> {

/* Ao herdar os comportamentos dessa interface, a nossa interface já recebe automaticamente métodos para criar, alterar, excluir e consultar os dados
 * (https://docs.spring.io/spring-data/commons/docs/current/reference/html/#repositories.core-concepts)
 * Mas, nesse projeto, criamos um método adicional usando palavras-chave de consulta (https://docs.spring.io/spring-data/commons/docs/current/reference/html/#repository-query-keywords)
 * Isso nos permite gerar uma lista de tarefas de acordo com seu status - feita ou não.
 * 
 * Note que o retorno desse método é do tipo Flux. Isso porque ele é o tipo que controla uma sequência de dados variando de zero a muitos
 * (https://docs.spring.io/spring-framework/reference/)
 */
        Flux<Todo> findByDone(boolean done);
    
}

/* É importante lembrar que o WebFlux está baseado no padrão publisher-subscriber,
 * portanto o retorno dos dados não é imediato como acontece em um método não reativo. 
 * O objeto é populado à medida que os dados são emitidos pelo publisher - nesse caso, o BD
 * e o "Flux" é projetado para isso.
 * 
 * Não é possível usar o Flux em um código não reativo. Ele não é apenas um container para dados,
 * como o List. Tem mais coisas acontecendo para tornar o código reativo.
 */

 /* Assim como foi feito  no projeto anterior, o código que implementa a interface TodoRepository
  * é criado automaticamente pelo Spring Data quando o projeto é compilado
  * Dessa forma, não precisamos nos preocupar com mais nada, nem mesmo com a configuração do MongoDB
  * apenas garantir que ele está disponível na porta padrão do computador quando o projeto for executado.
  * Como garante isso? vou descobrir....
 */