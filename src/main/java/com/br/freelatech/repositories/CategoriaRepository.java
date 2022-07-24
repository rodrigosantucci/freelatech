package com.br.freelatech.repositories;

import com.br.freelatech.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("categoriaRepository")
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

	Categoria findOne(Long categoria_id);

}
