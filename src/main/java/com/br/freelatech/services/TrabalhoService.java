package com.br.freelatech.services;


import com.br.freelatech.models.Trabalho;
import com.br.freelatech.models.Usuario;
import com.br.freelatech.repositories.TrabalhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class TrabalhoService {
	

    @Autowired
    TrabalhoRepository trabalhoRepository;

    public Trabalho get(long id){
        return trabalhoRepository.findOne(id);
    }

    public Trabalho add(Trabalho trabalho){

        trabalho.setTipo( trabalho.getTipo().toLowerCase() );
        trabalho.setExperiencia( trabalho.getExperiencia().toLowerCase() );

        return trabalhoRepository.save(trabalho);
    }

    public List<Trabalho> list(){

        List<Trabalho> result = trabalhoRepository.findAll();

        result.sort( (j1,j2) -> {
            return j1.getTrabalho_id() > j2.getTrabalho_id() ? -1 : 0;
        } );

        return result;
    }
    
    public List<Trabalho> list(Map<String,Object> filter){

        List<Trabalho> result = null;
        Usuario usr = (Usuario) filter.get("usuario");
        
        if( usr != null) {
        	result = trabalhoRepository.findByAutor(usr);
        }

        if(result != null) {
	        result.sort( (j1,j2) -> {
	            return j1.getTrabalho_id() > j2.getTrabalho_id() ? -1 : 0;
	        } );
        }

        return result;
    }
    
    public List<Trabalho> findByAutor(Usuario usuario){
    	return trabalhoRepository.findByAutor(usuario);
    }

	public List<Trabalho> findContratadoTrabalhosByAutor(Usuario usuario){
		return trabalhoRepository.findByAutorContratado(usuario);
	}
	
	public Page<Trabalho> findAllPaged(Map<String, Object> filter, Integer pageNumber, int pageSize) {
        
		PageRequest request = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "id");
        
        Usuario usuario = (Usuario) filter.get("usuario");
        if(usuario != null) {
        	return trabalhoRepository.findByAutor(usuario, request);
        } else {
        	return trabalhoRepository.findAll(request);
        }
    
	}
}
