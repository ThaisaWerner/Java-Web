package br.edu.utfpr.cp.espjava.crudcidades.usuaria;

import org.springframework.data.jpa.repository.JpaRepository;

/* Semana 07 - Criando interface de acesso à tabela
 * 
 * Além da classe de entidade, precisamos da interface que gerencia as operações de persistência, então criamos o Repository
 */
public interface UsuariaRepository extends JpaRepository<Usuaria, Long> {

/* Método que retorna uma usuária com base no nome informado. Esse método é necessário para integrar com o mecanismo de autenticação do
 * Spring Security
 */
    public Usuaria findByNome(String nome);
    
}
