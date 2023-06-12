package br.edu.utfpr.cp.espjava.crudcidades.visao;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CidadeController {

// Usando Set para armazenar a lista de cidades. O código anterior não possuía essa parte e mantinha a lista imutável e armazenada numa variável local.
// Para que a lista seja acessável por outros métodos, transformamos ela em um atributo de classe, conforme abaixo.
    private Set<Cidade> cidades;

// Iniciando uma lista vazia para armazenar as cidades inseridas na aplicação
// O HashSet é uma das implementações do Set que faz parte das colletions framework do Java
    public CidadeController() {
        cidades = new HashSet<>();
    }

    @GetMapping("/")

    public String listar(Model memoria) {

        memoria.addAttribute("listaCidades", cidades);

        return "/crud";
    }

// Método responsável por receber os dados do formulário e adicionar a nova cidade na lista.

/* Anotação que vai mapear o endereço do formulário com o método criar. 
 * A URL deve ser a mesma usada na propriedade action do formulário na página crud (for action="/criar").
*/
    @PostMapping("/criar")
/* Ele recebe um objeto do tipo cidade (Cidade cidade) como parâmetro 
 * Retorna uma String que representa a página a ser carregada. 
*/
    public String criar(Cidade cidade) {

        cidades.add(cidade);
/* Nesse caso, o desejado é que o usuário seja enviado para a página crud, mas queremos também que a lista de cidades seja carregada e mostrada para o usuário na tabela. 
 * Então, por isso ao invés de retornar o nome da página, retornamos um "redirect" que representa um redirecionamento para o método listar, que por sua vez, 
 * carrega a lista de cidades e chama também a página crud.
 */
        return "redirect:/";
    }

/* Método excluir que recebe duas Strings como parâmetro (cidade e estado) e retorna uma String. */

/* Anotação que mapeia a URL enviada pelo botão excluir no crud com esse método aqui no controller. */
    @GetMapping("/excluir")
    public String excluir(
    
    /* A anotação @RequestParam mapeia os parâmetros enviados pelo formulário com os do método excluir.
     * Os nomes dos parâmetros no formulário e aqui no método são iguais. Assim como no método criar, eles devem ser iguais para que
     * o mapeamento seja feito de forma automática.
     */
        @RequestParam String nome,
        @RequestParam String estado) {

        /* O removeIf também é um método do collections framework e estamos utlizando-o para remover a cidade passada da lista de cidades
         * Para isso estamos utilizando o atributo cidades que é do tipo Set
         * O removeIf usa Lambda como parâmetro. Para iterar pela lista vamos usar uma variável chamada cidadeAtual. 
         * Então verificamos se nome e estado da cidadeAtual é igual ao nome e estado passado como parâmetro para o método excluir.
         * A comparação das strings está sendo feita pelo método equals().
         */
            cidades.removeIf(cidadeAtual -> 
                cidadeAtual.getNome().equals(nome) &&
                cidadeAtual.getEstado().equals(estado)
            );

        /* Aqui redirecionamos o resultado desse método para o método listar(). */
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
            Cidade cidade
        ) {

            cidades.removeIf(cidadeAtual -> 
                                cidadeAtual.getNome().equals(nomeAtual) && 
                                cidadeAtual.getEstado().equals(estadoAtual)
            );

            criar(cidade);

            return "redirect:/";
        }
}