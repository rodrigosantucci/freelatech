package com.br.freelatech.controllers;

import com.br.freelatech.models.Proposta;
import com.br.freelatech.models.Feedback;
import com.br.freelatech.models.Usuario;
import com.br.freelatech.services.PropostaService;
import com.br.freelatech.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/feedback")
public class FeedbackController extends AbstratoController {

    private final String ROLE_CLIENTE = "cliente";
    private final String ROLE_CONTRATADO = "contratado";

    @Autowired
    PropostaService propostaService;

    @Autowired
    FeedbackService feedbackService;

    @GetMapping("/{propostaId}")
    public String enviar(@PathVariable("propostaId") long propostaId, Model model) throws Exception {

        Proposta proposta = propostaService.get(propostaId);

        if (proposta == null) {
            throw new Exception("A proposta não existe");
        }

        if (!possoPostar(proposta)) {
            throw new Exception("Usuário não é contratante ou contratado deste serviço!");
        }

        Feedback feedback = feedbackService.findByProposta(proposta);

        if (feedbackJaEnviado(feedback)) {
            return "redirect:/feedback/ver/" + proposta.getId();
        }

        String minhaRoleParaProposta = getMinhaRole(proposta);
        String profissional_nome = minhaRoleParaProposta.equals(ROLE_CLIENTE)
                ? proposta.getTrabalho().getAutor().getNome()
                : proposta.getUsuario().getNome();

        model.addAttribute("proposta", proposta);
        model.addAttribute("profissional_nome", profissional_nome);

        return "/feedback/enviar";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam(name = "proposta_id", required = true) Long propostaId,
            @RequestParam(name = "avaliacao", required = true) int avaliacao,
            @RequestParam(name = "feedback", required = true) String feedbackTexto) throws Exception {

        if (avaliacao < 1 || avaliacao > 5) {
            throw new Exception("A Avaliacao deve ser entre 1 e 5");
        }
        if (feedbackTexto.length() < 5) {
            throw new Exception("Insira um texto de avaliação");
        }

        Proposta proposta = propostaService.get(propostaId);

        if (!possoPostar(proposta)) {
            throw new Exception("Usuário não é contratante ou contratado deste serviço!");
        }

        Feedback feedback = new Feedback();
        feedback.setProposta(proposta);

        String minhaRoleParaProposta = getMinhaRole(proposta);

        if (minhaRoleParaProposta.equals(ROLE_CONTRATADO)) {
            feedback.setClienteFeedback(feedbackTexto);
            feedback.setClienteAvaliacao(avaliacao);
        } else {
            feedback.setContratanteFeedback(feedbackTexto);
            feedback.setContratanteAvaliacao(avaliacao);
        }

        Feedback dbFeedback = feedbackService.findByProposta(proposta);
        if (dbFeedback == null) {
            dbFeedback = feedbackService.salvar(feedback); // Insert
        } else {

            if (minhaRoleParaProposta.equals(ROLE_CONTRATADO)) {
                dbFeedback.setClienteFeedback(feedback.getClienteFeedback());
                dbFeedback.setClienteAvaliacao(feedback.getClienteAvaliacao());
            } else {
                dbFeedback.setContratanteFeedback(feedback.getContratanteFeedback());
                dbFeedback.setContratanteAvaliacao(feedback.getContratanteAvaliacao());
            }

            dbFeedback = feedbackService.salvar(dbFeedback); // Atualizar

        }

        return "redirect:/feedback/ver/" + proposta.getId();
    }

    @GetMapping("ver/{propostaId}")
    public String verPropostaFeedbacks(@PathVariable("propostaId") long propostaId, Model model) throws Exception {

        Proposta proposta = propostaService.get(propostaId);

        if (!possoPostar(proposta)) {
            throw new Exception("Usuário não é contratante ou contradado deste serviço!");
        }

        Feedback feedback = feedbackService.findByProposta(proposta);

        model.addAttribute("proposta", proposta);
        model.addAttribute("feedback", feedback);
        model.addAttribute("pode_postar_feedback", !feedbackJaEnviado(feedback) && possoPostar(proposta));

        return "/feedback/ver";

    }

    private boolean possoPostar(Proposta proposta) {
        Usuario eu = getUsuarioAtual();
        return eu.getId() != proposta.getUsuario().getId() || eu.getId() != proposta.getTrabalho().getAutor().getId();
    }

    private boolean feedbackJaEnviado(Feedback feedback) {
        if (feedback == null) {
            return false;
        }
        String minhaRole = getMinhaRole(feedback.getProposta());

        if (feedback.getClienteAvaliacao() != null && feedback.getContratanteAvaliacao() != null) {

            return (minhaRole.equals(ROLE_CONTRATADO) && feedback.getClienteAvaliacao() > 0)
                    || (minhaRole.equals(ROLE_CLIENTE) && feedback.getContratanteAvaliacao() > 0);

        } else {
            return false;
        }

    }

    private String getMinhaRole(Proposta proposta) {
        return getUsuarioAtual().getId().equals(proposta.getUsuario().getId()) ? ROLE_CONTRATADO : ROLE_CLIENTE;
    }

}
