package com.br.freelatech.controllers;

import com.br.freelatech.models.Usuario;
import com.br.freelatech.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class AbstratoController {

    @Autowired
    UsuarioService usuarioService;
    /**
     * Recupera usuario logado
     *
     * @return models.Usuario
     **/
    protected Usuario getUsuarioAtual(){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails == false) {
            return null;
        }
        String nome = ((UserDetails) principal).getUsername();

        return  usuarioService.getByEmail(nome);
    }
}
