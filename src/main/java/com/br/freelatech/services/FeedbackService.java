package com.br.freelatech.services;


import com.br.freelatech.models.Proposta;
import com.br.freelatech.models.Feedback;
import com.br.freelatech.models.Trabalho;
import com.br.freelatech.models.Usuario;
import com.br.freelatech.repositories.FeedbackRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
    

    @Autowired
    FeedbackRepository feedbackRepository;

    public Feedback salvar(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }


    public Feedback get(Long feedback_id) {
        return feedbackRepository.findOne(feedback_id);
    }

    public Feedback findByProposta(Proposta proposta) {
        return feedbackRepository.findByProposta(proposta);
    }


    public List<Feedback> findByCliente(Usuario usuario) {
        return feedbackRepository.findByCliente(usuario);
    }

    public Feedback findByTrabalho(Trabalho trabalho) {
        return feedbackRepository.findByTrabalho(trabalho);
    }

    public List<Feedback> findByPropostas (List<Proposta> propostas) {
        return feedbackRepository.findByProposta(propostas);
    }
}
