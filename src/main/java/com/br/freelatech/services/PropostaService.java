package com.br.freelatech.services;

import com.br.freelatech.models.Proposta;
import com.br.freelatech.models.Trabalho;
import com.br.freelatech.models.Usuario;
import com.br.freelatech.repositories.PropostaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PropostaService {
    
    @Autowired
    PropostaRepository propostaRepository;


    public Proposta salvar(Proposta proposta) {
        return propostaRepository.save(proposta);
    }

    public Proposta get(Long proposta_id) {
        return propostaRepository.findOne(proposta_id);
    }

    public Proposta getUsuarioPropostaPorTrabalho(Usuario usuario, Trabalho trabalho) {

        List<Proposta> propostas = propostaRepository.findByUsuarioIdAndTrabalhoId(usuario.getUsuario_id(), trabalho.getTrabalho_id());

        if(propostas.isEmpty()){

            return null;
        }


        if(propostas.size() > 1) {
            System.out.println("ERRO: encontrados mais de uma proposta do usuario para o trabalho.");
        }

        try {
            return propostas.get(0);
        } catch (IndexOutOfBoundsException e ) {
            System.out.println("ERRO: Não foram encontrados propostas para o usuario");
        }

        return null;

    }

    public List<Proposta> findPorUsuario(Usuario usuario) {
        return propostaRepository.findByUsuario(usuario);
    }

    public List<Proposta> findByTrabalho(Trabalho trabalho) {
        return propostaRepository.findByTrabalho(trabalho);
    }

    public boolean aceitaProposta(Proposta proposta) {
        proposta.setPropostaAceita(1);
        salvar(proposta);
        return true;
    }

    public List<Proposta> findByUsuarioTrabalhos(Usuario usuario) {
        return propostaRepository.findByUsuarioTrabalhos(usuario);
    }

    public List<Proposta> findByFechadoAndUsuario(int statusProposta, Usuario usuario) {
        return propostaRepository.findByFechadoAndUsuario(statusProposta, usuario);
    }

}
