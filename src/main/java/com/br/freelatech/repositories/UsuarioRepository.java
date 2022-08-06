package com.br.freelatech.repositories;

import com.br.freelatech.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository("usuarioRepository")
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	
	Usuario findByEmail(String email);

}
