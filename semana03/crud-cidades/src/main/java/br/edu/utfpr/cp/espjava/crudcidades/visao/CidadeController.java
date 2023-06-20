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

    /* Depois de alterarmos o botão no arquivo crud.ftl, criamos aqui o método que será responsável por tratar a solicitação de alteração quando
     * a usuária clica no botão de alterar. */
        @GetMapping("/preparaAlterar") /* Anotação que faz o mapeamento com a URL da alteração, a mesma que colocamos no arquivo crud.ftl */
        public String preparaAlterar(

        /* Assim como no método excluir, esse método recere dois parâmetros que representam o nome da cidade e do estado.
         * O método de alterar deve recuperar a cidade e o estado que a usuária informou pelos parâmetros e colocar essas informações na
         * memória da solicitação e recarregar a página. Para isso utilizamos um outro parâmetro do tipo "org.springframework.ui.Model", 
         * que é o Model memoria. */
            @RequestParam String nome,
            @RequestParam String estado,
            Model memoria

        ) {

        /* Agora, o método precisa recuperar essas informações do model e colocá-las na memória. Para isso usamos o atributo cidades para que
         * tenhamos acesso a lista de cidades e filtramos (.filter) apenas a cidade na qual o nome e o estado são iguais as informações que 
         * foram passadas como parâmetros (getNome(). equals....). 
         * 
         * Como terá apenas uma combinação cidade/estado, podemos usar o método findAny() para recuperar um objeto (java.util.Optional) que 
         * encapsule a cidade buscada*/
            var cidadeAtual = cidades
                                .stream()
                                .filter(cidade -> 
                                            cidade.getNome().equals(nome) && 
                                            cidade.getEstado().equals(estado)
                                ).findAny();

        /* Se existir uma cidade que bata com as informações buscadas, ela será armazenada em um objeto do tipo Optional que será acessível pela
         * variável cidadeAtual.
         * O último passo é verificar se "cidadeAtual" não está vazia, caso não esteja adicionamo-as na memória da solicitação (memoria.addAttribute...).
         * 
         * Perceba que também adicionamos a lista de cidades na memoria. Precisamos disso porque esse método não fará um redirecionamento, ao invés disso,
         * o método vai carregar a página diretamente. Então, se quisermos ver a lista de cidades no fim do processo, precisamos colocá-la na memória da
         * solicitação.
         * Nós não redirecionamos porque cada solicitação tem um tempo de vida, só existindo um caminho: o de ida ou o de volta, então se fizermos
         * o redirecionamento, nesse caso o valor da "cidadeAtual" se perderá.
         * 
         * O último passo é retornar o nome da página carregada (return...)
         */
            if (cidadeAtual.isPresent()) {
                memoria.addAttribute("cidadeAtual", cidadeAtual.get());
                memoria.addAttribute("listaCidades", cidades);
            }

            return "/crud";
        }

    /* Esse método vai efetivamente enviar os dados para alterá-los na lista de cidades.
     * Ele recebe três parâmetros. Os dois primeiros, representam os nomes atuais da cidade e do estado que devem ser alterados e o terceiro representa
     * os novos valores que a usuária digitou e são recebidos da mesma maneira que acontece no método criar().
     * Temos também a anotação de mapeamento do método com a URL de alteração enviada pelo crud (a mesma URL que está lá no crud).
     */
        @PostMapping("/alterar")
        public String alterar(
            @RequestParam String nomeAtual,
            @RequestParam String estadoAtual,
            Cidade cidade
        ) {

        /* Ai primeiro precisamos remover a cidade atual, da mesma forma que foi feito no método excluir. Isso porque na prática cada cidade é imutável,
         * não conseguimos alterar seus dados. Por isso, removemos e inserimos uma nova cidade com os novos dados.
         * Aqui reutilizamos o método criar() porque ele faz exatamente o que queremos: insere os novos valores e redireciona para a página inicial.
         */
            cidades.removeIf(cidadeAtual -> 
                                cidadeAtual.getNome().equals(nomeAtual) && 
                                cidadeAtual.getEstado().equals(estadoAtual)
            );

            criar(cidade);

        /* O retorno nunca é usado de fato pois o método criar já faz isso. Mas ainda assim o colocamos para evitar erro de compilação. */
            return "redirect:/";
        }
}