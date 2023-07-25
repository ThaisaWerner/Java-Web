package br.edu.utfpr.cp.espjava.crudcidades.cidade;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/* Cookies
 * Representam informações que são armazenadas como um par de valores do tipo texto no navegador da usuária. Um desses valores representa
 * o nome do cookie e o outro o conteúdo armazenado nesse cookie.
 * Eles são bastante comuns em páginas web. Quando um site informa que "seus dados serão armazenados para melhorar sua experiência no site"
 * normalmente esses dados são salvos em cookies.
 * Diferente da sessão, um cookie armazena os dados no navegador da usuária ao invés de em um espaço de memória do servidor. 
 * Isso é importante porque a memória do servidor é encerrada no logout, enquanto o cookie pode permanecer no navegador.
 * Outra diferença é que o cookie armazena um par de Strings, enquanto uma sessão pode armazenar um objeto.
 * Dessa forma, sempre que acessamos uma determinada página, seu aplicativo pode verificar a existência de cookies criados previamente.
 * Se eles existirem, você pode aproveitar os dados para melhorar a experiência da usuária e indicar produtos relacionados com pesquisas
 * prévias, por exemplo.
 */

/* Salvando o nome da usuária na sessão */
@Controller
public class CidadeController {

    private final CidadeRepository repository;

    public CidadeController(CidadeRepository repository) {
        this.repository = repository;
    }

/* Vamos usar esse método para identificar a usuária atual e também para salvar seu nome na sessão atual. Para isso, adicionamos dois
 * parâmetros no método: Principal, que representa a usuária logada na aplicação e HttpSession, que representa a sessão atual.
 * PS: existem vários objetos no Spring que podem ser injetados automaticamente como parâmetros, aqui tem uma lista deles: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/arguments.html
 */
    @GetMapping("/")
    public String listar(Model memoria, Principal usuaria, HttpSession sessao, HttpServletResponse response) {

    /* O gerenciamento de cookies no Spring Boot é muito similar ao da sessão. O cookie também é injetado com injeção de dependência, dessa forma
     * tudo o que precisamos fazer é acessar o objeto e adicionar valores.
     * Para mostrar o uso de cookies, vamos gravar em um cookie o momento em que cada operação do CRUD foi acessada pela última vez. 
     * Adicionamos acima um parâmetro do tipo HttpServlet... que representa o resultado de uma solicitação que é enviada como retorno para o navegador.
     * Esse objeto tem o método addCookie() que recebe como parâmetro um objeto do tipo Cookie, representando um par de valores String,
     * o primeiro é o nome do cookie e o segundo é o valor que o cookie representa.
     * O objeto LocalDateTime representa o dia e hora atuais.
     */
        response.addCookie(new Cookie("listar", LocalDateTime.now().toString()));

        memoria.addAttribute("listaCidades", repository.findAll().stream().map(Cidade::clonar).collect(Collectors.toList()));

    /* Aqui usamos usuaria para acessar o método getName que retorna o nome da usuária logada no sistema.
     * Também usamos sessao para acessar setAttribute que recebe dois parâmetros: o primeiro, é uma String que define o nome do atributo
     * que será criado na sessão e o segundo é o objeto que será armazenado na sessão.
     * Em síntese, sessao é usada para criar um atributo na sessão com o nome de usuariaAtual e o nome da usuária atual, que é extraído com
     * usuaria.getName, é armazenado como o valor do atributo usuariaAtual.
     */
        sessao.setAttribute("usuariaAtual", usuaria.getName());

        return "/crud";
    }

/* Adicionamos o cookie aqui também, da mesma forma que no método listar */
    @PostMapping("/criar")
    public String criar(@Valid Cidade cidade, BindingResult validacao, Model memoria, HttpServletResponse response) {

        response.addCookie(new Cookie("criar", LocalDateTime.now().toString()));

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
            memoria.addAttribute("listaCidades", repository.findAll());

            return ("/crud");

        } else {
            repository.save(cidade.clonar());
        }

        return "redirect:/";
    }

/* Adicionamos o cookie aqui também, da mesma forma que no método listar */
    @GetMapping("/excluir")
    public String excluir(
            @RequestParam String nome,
            @RequestParam String estado,
            HttpServletResponse response) {

        response.addCookie(new Cookie("excluir", LocalDateTime.now().toString()));
            
        var cidadeEstadoEncontrada = repository.findByNomeAndEstado(nome, estado);

        cidadeEstadoEncontrada.ifPresent(repository::delete);

        return "redirect:/";
    }

    @GetMapping("/preparaAlterar")
    public String preparaAlterar(
        @RequestParam String nome,
        @RequestParam String estado,
        Model memoria
    ) {

        var cidadeAtual = repository.findByNomeAndEstado(nome, estado);

        cidadeAtual.ifPresent(cidadeEncontrada -> {
            memoria.addAttribute("cidadeAtual", cidadeEncontrada);
            memoria.addAttribute("listaCidades", repository.findAll());
        });

        return "/crud";
    }

/* Adicionamos o cookie aqui também, da mesma forma que no método listar */
    @PostMapping("/alterar")
    public String alterar(
        @RequestParam String nomeAtual,
        @RequestParam String estadoAtual,
        Cidade cidade,
        HttpServletResponse response){

            response.addCookie(new Cookie("alterar", LocalDateTime.now().toString()));
        
            var cidadeAtual = repository.findByNomeAndEstado(nomeAtual, estadoAtual);

            if (cidadeAtual.isPresent()) {
                var cidadeEncontrada = cidadeAtual.get();
                cidadeEncontrada.setNome(cidade.getNome());
                cidadeEncontrada.setEstado(cidade.getEstado());

                repository.saveAndFlush(cidadeEncontrada);
            }

        return "redirect:/";
    }

/* Criando método para exibir/ler cookie
 * O método retorna uma String que representa o resultado que deve ser apresentado na página web.
 * Ele recebe uma String como parâmetro que representa o valor armazenado no cookie.
 * O nome da variável de acesso ao parâmetro deve ser o mesmo usado para nomear o cookie, no exemplo abaixo usamos listar, que
 * é o nome do cookie criado pra guardar o horário do último acesso ao método listar().
 * Mapeamos o método com a URL /mostrar e como esse método retorna uma String ao invés de uma página web, adicionamos também 
 * a anotação ResponseBody.
 * A anotação @CookieValue é usada pelo Spring Boot para mapear o cookie cujo nome corresponde ao nome do parâmetro passado,
 * nesse caso, listar.
 */
    @GetMapping("/mostrar")
    @ResponseBody
    public String mostraCookieAlterar(@CookieValue String listar) {
        return "Último acesso ao método listar(): " + listar;
    }
}