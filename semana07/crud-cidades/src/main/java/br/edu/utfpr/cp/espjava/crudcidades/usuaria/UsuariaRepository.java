package br.edu.utfpr.cp.espjava.crudcidades.usuaria;

import org.springframework.data.jpa.repository.JpaRepository;

/* Semana 07 - Criando interface de acesso à tabela */
public interface UsuariaRepository extends JpaRepository<Usuaria, Long> {

    public Usuaria findByNome(String nome);
    
}
