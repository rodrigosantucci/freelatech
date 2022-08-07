package com.br.freelatech.repositories;


import com.br.freelatech.models.Usuario;
import com.br.freelatech.models.Trabalho;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;




@Repository("trabalhoRepository")
public interface TrabalhoRepository extends JpaRepository<Trabalho, Long>{
	List<Trabalho> findByAutor(Usuario autor);
	
	
	@Query("SELECT t "
			+"FROM proposta p "
			+"JOIN p.trabalho t "
			+"WHERE t.autor = :usuario AND p.aceita = 1")
	List<Trabalho> findByAutorContratado(@Param("usuario") Usuario usuario);
	
	Page<Trabalho> findByAutor(Usuario usuario, Pageable request);
	
}
