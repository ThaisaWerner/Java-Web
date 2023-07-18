package br.edu.utfpr.cp.espjava.crudcidades.usuaria;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/* Semana 07 - Classe reponsável por carregar usuária no BD 
 * É obrigatório implementar a interface abaixo!
 * 
 * Com as implementações feitas na classe Usuaria, o Spring Security entende que ela é uma classe que representa uma usuária do sistema.
 * Agora, temos que definir uma implementação do serviço que verifica a existência da usuária, por isso criamos essa classe aqui.
 * 
 * A anotação service indica ao Spring Boot que ela deve ser gerenciada pelo framework.
*/
@Service
public class UsuariaDetailsService implements UserDetailsService {

    private final UsuariaRepository usuariaRepository;

    public UsuariaDetailsService(final UsuariaRepository usuariaRepository) {
        this.usuariaRepository = usuariaRepository;
    }

/* O UserDetailsService define o método abaixo.
 * Nossas usuárias está no BD, então precisamos verificar se a usuária passada como parâmetro nesse método existe de fato no nosso BD.
 */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
    /* No usuariaRepository já foi criado um método que faz isso. Para podermos usar UsuariaRepository precisamos criar um construtor para que
     * o Spring Boot faça a injeção de dependência (feito acima).
     */
        var usuaria = usuariaRepository.findByNome(username);

    /* E por fim, se a usuária não existir no BD, é disparada uma exceção que é usada pelo Spring Security para dizer que houve algum
     * problema na autenticação.
     */
        if (usuaria == null) {
            throw new UsernameNotFoundException("Usuária não encontrada");
        }

        return usuaria;
    }
    
}
