package br.edu.utfpr.cp.espjava.crudcidades.cidade;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

/* Semana 05 -  Banco de dados
 * O java permite acesso a uma ampla variedade de SGBDs (Sistema gerenciadores de banco de dados) por meio de drivers específicos.
 * Esses drivers fornecem implementações do JDBC (Java Database Connectivity) para o SGDB específico.
 * Apesar de aqui estarmos usando o JPA para fazer a persistência, esses drivers são essenciais para que o próprio JPA funcione.
 * Por isso, a primeira coisa a se fazer é inserir a dependência lá no pom que adiciona o driver do SGBD ao nosso projeto (mais explicações no pom).
*/
@Controller
public class CidadeController {

    private final CidadeRepository repository;

/* Usando o CidadeRepository para realizar a persistência de dados no banco */
    public CidadeController(final CidadeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String listar(Model memoria) {

    /* Convertendo cidadeEntity em cidade */
    /* Ao invés de usarmos a lista de cidades, agora usaremos o atributo repository para acessar as cidades armazenadas no BD  
     * O método findAll() do repository retorna uma lista do tipo CidadeEntity, mas como o nosso MVC está usando Cidade como referência,
     * precisamos converter os dados.
    */
        memoria.addAttribute("listaCidades", this.converteCidade(repository.findAll()));

        return "/crud";
    }

    private List<Cidade> converteCidade(List<CidadeEntity> cidades) {
        return cidades.stream()
                .map(cidade -> new Cidade(
                    cidade.getNome(), 
                    cidade.getEstado()))
                .collect(Collectors.toList());

    }

    @PostMapping("/criar")
    public String criar(@Valid Cidade cidade, BindingResult validacao, Model memoria) {

        if (validacao.hasErrors()) {
            validacao
                .getFieldErrors()
                .forEach(
                        error -> memoria.addAttribute(
                            error.getField(), 
                            error.getDefaultMessage()) 
                );

            memoria.addAttribute("nomeInformado", cidade.getNome());
            memoria.addAttribute("estadoInformado", cidade.getEstado());
            memoria.addAttribute("listaCidades", this.converteCidade(repository.findAll()));
            return "/crud";

 /* Ao invés de inserir a cidade informada pela usuária na lista de cidades, vamos usar o atributo repository para
* persistir os dados da cidade no BD. Lembrando que estamos usando a Cidade no MVC, mas a CidadeEntity para a persistência,
* então precisamos fazer a conversão dos dados.
* Depois que o convertemos, chamamos o método sabe() do JPA repository para persistir os dados em CidadeEntity
* Também observe que a persistência só acontece se passar pela validação acima.
*/
        } else {
            repository.save(cidade.clonar());
        }

        return "redirect:/";
    }

/* Tentamos recuperar a CidadeEntity de acordo com os dados informados pela usuária, e se ela existir, removemo-as do BD */
    @GetMapping("/excluir")
    public String excluir(
            @RequestParam String nome,
            @RequestParam String estado) {
            
    /* Método feito no CidadeRepository */
        var cidadeEstadoEncontrada = repository.findByNomeAndEstado(nome, estado);

        cidadeEstadoEncontrada.ifPresent(repository::delete);

        return "redirect:/";
    }

/* Ajustando o método
 * Antes, esse método utilizava um filtro para buscar uma cidade pelo nome e estado (var cidadeAtual = cidades.stream...), mas podemos usar o método de busca
 * que fizemos no CidadeRepository para substituir esse antigo filtro.
 * 
 * Outra coisa que o método fazia, era gravar a cidade atual e a lista de cidades na memória da solicitação para caso a cidade buscada existisse. Substituímos essa
 * estrutura pelo método Optional.ifPresent(), semelhante ao que fizemos no método excluir(), assim os valores que são gravados na solicitação para serem usados na
 * página web serão buscados no BD ao invés da lista.
 */
    @GetMapping("/preparaAlterar")
    public String preparaAlterar(
        @RequestParam String nome,
        @RequestParam String estado,
        Model memoria
    ) {

    /* Usando o método do Repository */
        var cidadeAtual = repository.findByNomeAndEstado(nome, estado);

        cidadeAtual.ifPresent(cidadeEncontrada -> {
            memoria.addAttribute("cidadeAtual", cidadeEncontrada);
            memoria.addAttribute("listaCidades", this.converteCidade(repository.findAll()));
        });

        return "/crud";
    }

/* Ajustando método alterar
 * Ele precisa de dois ajustes: Primeiro precisamos substituir a exclusão da cidade existente da lista pela busca no BD, isso porque agora, ao invés de excluir e criar
 * novamente uma cidade, alteramos os dados da cidade existente. E depois substituímos a chamada do método criar() pelo método do repository para alterar a cidade existente.
 */
    @PostMapping("/alterar")
    public String alterar(
        @RequestParam String nomeAtual,
        @RequestParam String estadoAtual,
        Cidade cidade,
        BindingResult validacao,
        Model memoria){
        
        /* Usamos o mesmo método do repository para buscar a cidade existente */
            var cidadeAtual = repository.findByNomeAndEstado(nomeAtual, estadoAtual);

        /* Ai se ela é encontrada, os dados são atualizados no BD */
            if (cidadeAtual.isPresent()) {
                var cidadeEncontrada = cidadeAtual.get();
                cidadeEncontrada.setNome(cidade.getNome());
                cidadeEncontrada.setEstado(cidade.getEstado());

                repository.saveAndFlush(cidadeEncontrada);
            }

        return "redirect:/";
    }
}