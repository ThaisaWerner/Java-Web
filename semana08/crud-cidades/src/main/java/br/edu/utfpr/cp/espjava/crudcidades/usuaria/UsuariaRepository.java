package br.edu.utfpr.cp.espjava.crudcidades.usuaria;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariaRepository extends JpaRepository<Usuaria, Long> {

    public Usuaria findByNome(String nome);
    
}
