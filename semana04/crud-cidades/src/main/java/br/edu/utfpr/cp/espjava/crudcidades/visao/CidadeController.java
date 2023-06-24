package br.edu.utfpr.cp.espjava.crudcidades.visao;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@Controller
public class CidadeController {

    private Set<Cidade> cidades;

    public CidadeController() {
        cidades = new HashSet<>();
    }

    @GetMapping("/")
    public String listar(Model memoria) {
        memoria.addAttribute("listaCidades", cidades);
        return "/crud";
    }

    /* Apenas inserir as anotações de validação não é o suficiente, então precisamos indicar para os métodos que eles precisam usar a validação quando
       receberem a classe Cidade como parâmetro de uma solicitação. Para isso adicionamos a anotação @Valid antes do parâmetro Cidade. 
       Isso faz com que o Spring acione a validação no método quando, nesse caso, ele receber um objeto do tipo cidade. E essas validações serão feitas conforme
       as anotações lá da classe Cidade. 

       Quando acontecer toda essa validação, os erros que eventualmente forem gerados, serão enviados para um objeto do tipo BindingResult, por isso o definimos 
       como um parâmetro no método.*/
    @PostMapping("/criar")
    public String criar(@Valid Cidade cidade, BindingResult validacao, Model memoria) { //O model é responsável por carregar dados numa solicitação, seja ela indo ou vindo de GUI

    /* Adicionando o BindingResult, usamos os métodos nele disponíveis para verificar a existência de erros.
     * Se sim, vamos imprimí-los e suas mensagens no console do sistema.
     * 
     * Observe que a criação de uma nova cidade agora está condicionada a não existência de problemas na validação.
     * 
     * Caso haja erros, o método getFieldErrors() irá retornar uma lista de erros. Nesse caso, foi usado o forEach para iterar a lista e imprimir os erros no console.
     * Esse objeto (BindingResult) fornece dois métodos que permitem identificar o atributo onde o erro foi detectado (getField) e a mensagem de erro associada
     * (getDefaultMessage)
     */
        if (validacao.hasErrors()) {
            validacao
                .getFieldErrors()
                .forEach(
                    /* Adicionando os erros como atributos da variável memória. Ao invés de imprimir os erros no console, usamos essa variável (memória)
                     * para guardar os erros como atributos.
                     */
                        error -> memoria.addAttribute(
                            error.getField(), //Onde o erro ocorreu
                            error.getDefaultMessage()) //Mensagem de erro definida para o erro ocorrido (mensagens definidas no arquivo messages.properties)
                );

        /* Salvando valores digitados na memoria para que o usuário consiga visualizá-los e perceber seu erro.*/
            memoria.addAttribute("nomeInformado", cidade.getNome());
            memoria.addAttribute("estadoInformado", cidade.getEstado());
            memoria.addAttribute("listaCidades", cidades); //Adicionando a lista de cidades à memória antes de redirecionar a usuária para a página crud

            return "/crud";

        } else {

        /* Cidade é criada se não houver problemas de validação */
            cidades.add(cidade);
        }

        return "redirect:/";
    }

    @GetMapping("/excluir")
    public String excluir(
        @RequestParam String nome,
        @RequestParam String estado) {
            cidades.removeIf(cidadeAtual -> 
                cidadeAtual.getNome().equals(nome) &&
                cidadeAtual.getEstado().equals(estado)
            );
            return "redirect:/";
        }

    @GetMapping("/preparaAlterar")
    public String preparaAlterar(
        @RequestParam String nome,
        @RequestParam String estado,
        Model memoria
    ) {
        var cidadeAtual = cidades
                            .stream()
                            .filter(cidade -> 
                                        cidade.getNome().equals(nome) && 
                                        cidade.getEstado().equals(estado)
                            ).findAny();

        if (cidadeAtual.isPresent()) {
             memoria.addAttribute("cidadeAtual", cidadeAtual.get());
             memoria.addAttribute("listaCidades", cidades);
        }
        return "/crud";
    }

    @PostMapping("/alterar")
    public String alterar(
        @RequestParam String nomeAtual,
        @RequestParam String estadoAtual,
        Cidade cidade,
        BindingResult validacao,
        Model memoria){
        
         cidades.removeIf(cidadeAtual -> 
                            cidadeAtual.getNome().equals(nomeAtual) && 
                            cidadeAtual.getEstado().equals(estadoAtual)
        );

        criar(cidade, validacao, memoria);

        return "redirect:/";
    }
}