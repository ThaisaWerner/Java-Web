//classe que controla a requisição, representa o controller
//disbonibiliza uma lista da classe cidade para o crud por meio do ui.model... que não é a mesma coisa do model do MVC!!
//esse ui.model funciona como uma memória compartilhada entre o controller e o view e é usado pelo spring boot para permitir que
//os dados transitem entre os dois
package br.edu.utfpr.cp.espjava.crudcidades.visao;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//classe Java que será responsável por receber as solicitações do navegador e enviar a página para o navegador

//anotação que indica para o spring boot que essa classe recebe requisições vindas do navegador.
@Controller
public class CidadeController {

    //anotação que indica para o spring boot que qualquer solicitação feita para o endereço indicado (nesse caso a raiz da URL) deve ser entregue para
    //o método listar(). Quando o método retorna o nome da página, ele entende que deve carregar essa página
    @GetMapping("/")

    /* tornando essa lista de cidades disponível para que o freemarker consiga acessar e colocar os valores na página (utilizando o objeto Model)
    por isso foi criado o parâmetro dentro de listar(). Esse parâmetro representa uma espécie de memória compartilhada durante as requisições
    toda vez que uma requisição vier ou for enviada para o navegador, ela traz uma memória junto
    essa memória pode armazenar valores que precisa-se enviar ou receber do navegador, nesse caso, precisamos enviar a lista de cidades
    a classe org.springframework.ui.Model (model) tem o método addAttribute() que permite inserir um par de valores
    nesse caso, queremos criar uma variável listaCidades que é a mesma variável utilizada na página dinâmica (crud) pelo freemarker
    e como valor queremos enviar o set cidades, que atualmente tem as 3 cidades abaixo */
    public String listar(Model memoria) {

        //criando uma lista de cidades
        var cidades = Set.of(
            new Cidade("Pinhais", "PR"),
            new Cidade("Cornélio Procópio", "PR"),
            new Cidade("Salvador", "BA")
        );

        memoria.addAttribute("listaCidades", cidades);
        return "/crud";
    }
}