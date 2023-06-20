package br.edu.utfpr.cp.espjava.crudcidades.visao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/* Semana 04 - Definindo validações com bean validation. Adicionando anotações nos atributos. 
 * NotBlank impede que sejam aceitos valores nulos ou espaços em branco.
 * Size restringe um intervalo de valores de caracteres.
 * Message define a mensagem a ser exibida caso a exigência não seja atendida.
*/

/* Sobre mensagens direto na classe:
 * Não é uma boa ideia deixá-las diretamenta aqui pois, qualquer alteração nela, gerará recompilação.
 * Também dificulta que a aplicação seja distribuída para outros países, pois a mensagem deve estar na mesma língua da usuária.
 * E pode gerar erros por engano, caso ao alterarar a mensagem, o programador acabe alterando alguma outra coisa sem querer.
 * Por isso,  é interessante colocar a mensagem em um arquivo separado. Para isso precisaremos fazer algumas configurações extras, que serão feitas no arquivo
 * CrudCidadesApplication
 */
public final class Cidade {

    @NotBlank(message = "{app.cidade.blank}") //código da mensagem definida no arquivo messagem.properties - explicação no arquivo CrudCidadesApplication
    @Size(min = 5, max = 60, message = "{app.cidade.size}")
    private final String nome;

    @NotBlank(message = "{app.estado.blank}")
    @Size(min = 2, max = 2, message = "{app.estado.size}")
    private final String estado;

    public Cidade(final String nome, final String estado) {
        this.nome = nome;
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public String getNome() {
        return nome;
    }
    
}
