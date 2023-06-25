package br.edu.utfpr.cp.espjava.crudcidades.cidade;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/* Semana 05 - criando repositório 
 * Criando método de busca por nome e estado
*/
public interface CidadeRepository extends JpaRepository<CidadeEntity, Long> {
    public Optional<CidadeEntity> findByNomeAndEstado(String nome, String estado);
}
