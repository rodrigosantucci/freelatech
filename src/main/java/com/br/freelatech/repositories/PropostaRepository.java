package com.br.freelatech.repositories;

import com.br.freelatech.models.Proposta;
import com.br.freelatech.models.Trabalho;
import com.br.freelatech.models.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;



@Repository("tb_proposta")
public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    List<Proposta> findByUsuarioIdAndTrabalhoId(Long usuarioId, Long trabalhoId);
    List<Proposta> findByUsuario(Usuario usuario);
    List<Proposta> findByTrabalho(Trabalho trabalho);
    
    @Query("SELECT b"
			+ " FROM Proposta b"
			+ " JOIN b.trabalho j"
			+ " WHERE j.autor = :eu ")
	List<Proposta> findByUsuarioTrabalhos(@Param("eu") Usuario me);
	
    List<Proposta> findByFechadoAndUsuario(int closed, Usuario usuario);
    
}
