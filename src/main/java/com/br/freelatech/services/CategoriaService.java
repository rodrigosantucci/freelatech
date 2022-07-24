package com.br.freelatech.services;


import com.br.freelatech.models.Categoria;
import com.br.freelatech.repositories.CategoriaRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {
	
	@Autowired
	CategoriaRepository categoriaRepository;
	
	public Categoria get(Long categoria_id) {
		return categoriaRepository.findOne(categoria_id);
	}
	
	public Categoria add(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}
	
	public List<Categoria> list() {
		return categoriaRepository.findAll();
	}

}
