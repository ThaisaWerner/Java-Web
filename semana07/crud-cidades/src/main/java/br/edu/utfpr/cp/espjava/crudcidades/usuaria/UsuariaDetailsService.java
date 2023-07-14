package br.edu.utfpr.cp.espjava.crudcidades.usuaria;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/* Semana 07 - Classe reponsável por carregar usuária no BD 
 * É obrigatório implementar a interface abaixo!
*/
@Service
public class UsuariaDetailsService implements UserDetailsService {

    private final UsuariaRepository usuariaRepository;

    public UsuariaDetailsService(final UsuariaRepository usuariaRepository) {
        this.usuariaRepository = usuariaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        var usuaria = usuariaRepository.findByNome(username);

        if (usuaria == null) {
            throw new UsernameNotFoundException("Usuária não encontrada");
        }

        return usuaria;
    }
    
}
