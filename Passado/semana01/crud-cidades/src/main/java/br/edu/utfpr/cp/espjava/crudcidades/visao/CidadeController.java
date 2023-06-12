//classe que controla a requisição
package br.edu.utfpr.cp.espjava.crudcidades.visao;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//classe Java que será responsável por receber as solicitações do navegador e enviar a página para o navegador

//anotação que indica para o spring boot que essa classe recebe requisições vindas do navegador.
@Controller
public class CidadeController {

    //anotação que indica para o spring boot que qualquer solicitação feita para o endereço indicado (nesse caso a raiz da URL) deve ser entregue para
    //o método listar(). Quando o método retorna o nome da página, ele entende que deve carregar essa página
    @GetMapping("/")
    public String listar() {
        return "crud.html";
    }
}