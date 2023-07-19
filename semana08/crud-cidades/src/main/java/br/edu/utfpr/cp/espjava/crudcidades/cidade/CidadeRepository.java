package br.edu.utfpr.cp.espjava.crudcidades.cidade;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CidadeRepository extends JpaRepository<CidadeEntity, Long> {

    public Optional<CidadeEntity> findByNomeAndEstado(String nome, String estado);
    
}
