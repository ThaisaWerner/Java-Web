package com.example.demo;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/* Semana 06 - Seguindo o modelo MVC, o controlador tem a responsabilidade de gerenciar a interação entre o usuário e a API.
 * Nesse novo projeto, ele age como um delegador de solicitações. Então os métodos aqui, apenas expõem os endereços da API
 * e repassam as solicitações de e para a interface de persistência.
 * 
 * Existe uma outra maneira de fazer isso que é pelo Spring Data Rest que permite criar APIs diretamenta da interface do repositório,
 * assim, não é necessário criar um controlador para delegar solicitações.
 */

/* As anotações são as mesmas anotações usadas pelo Spring MVC, por isso, esse mesmo controlador poderia ser usado tanto para uma API com MVC quanto com WebFlux.
 * O que muda são os tipos de retornos dos métodos.
 * São as anotações que fazem com que o framework entenda que esse código deve ser gerenciado pelo Spring.
 */
@RestController
public class TodoRestController {

    private final TodoRepository repository;

/* Construtor que recebe o TodoRepository como argumento e é usado pelo Spring para injetar a dependência no atributo repository. 
 * (pesquisar + sobre injeção de dependência)
 * Dessa forma não precisamos instanciar o TodoRepository a nenhum momento. (Pesquisar a diferença entre injeção de dp e instanciação)
 */
    public TodoRestController(final TodoRepository repository) {
        this.repository = repository;
    }

/* Esse método é responsável por listar as tarefas existentes na base de dados. O corpo do método repassa a solicitação para uma instância de TodoRepository
 * responsável por acessar a base de dados. O resultado de findAll é retornado diretamente para a solicitante.
 */
    @GetMapping("/todos")
    public Flux<Todo> readAll() {
        return repository.findAll();
    }
    
/* Esse método retorna apenas as tarefas que tiverem do atributo done com o valor definido no parâmetro do método, que pode ser true or false para done.
 * Ele apenas repassa a solicitação, chamando o método findByDone lá do repository.
 */
    @GetMapping("/todos/{done}")
    public Flux<Todo> readByDone(@PathVariable boolean done) {
        return repository.findByDone(done);
    }

/* Responsável por salvar uma tarefa na base de dados. Ele possui um retorno diferente dos dois anteriores: enquanto eles podem retornar mais de uma tarefa, 
 * aqui só é possível retornar uma. o tipo Mono é similar ao Tipo Flux, mas ele suporta carregar apenas um objeto. Então nesse caso o retorno corresponde à tarefa
 * que foi criada.
 * Aqui também é a mesma coisa, o método repassa a solicitação para o método correspondente no TodoRepository.
 * O todo representa a tarefa que será criada.
 */
    @PostMapping("/todos")
    public Mono<Todo> create(@RequestBody Todo todo) {
        return repository.save(todo);
    }

/* Aqui, funciona da mesma maneira, mas recebe-se uma String como parâmetro que representa o identificador da tarefa e ele retorna um vazio, já que a tarefa
 * acabou de ser eliminada.
 */
    @DeleteMapping("/todos/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return repository.deleteById(id);
    }

/* Esse método alterna o estado da tarefa entre feito e não feito. */

    @PutMapping("/todos/{id}")
    public Mono<Todo> update(@PathVariable String id) {

        return repository
                /* Recupera a tarefa correspondente ao identificador informado */
                    .findById(id)
                /* Nova tarefa é criada, com os mesmos valores da tarefa existente */
                    .map(currentTodo -> new Todo(id, 
                                                currentTodo.title(), 
                                                currentTodo.description(), 
                                                !currentTodo.done())) //Mas aqui o valor do atributo é alternado
                /* A tarefa é salva  */
                    .flatMap(repository::save)
                /* Os "assinantes" da operação são liberados (?) */
                    .onTerminateDetach();
    }
}
