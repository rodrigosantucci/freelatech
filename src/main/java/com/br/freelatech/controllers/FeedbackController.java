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

    private final String ROLE_CLIENT = "cliente";
    private final String ROLE_CONTR = "contratante";

    @Autowired
    PropostaService propostaService;

    @Autowired
    FeedbackService feedbackService;

    @GetMapping("/{propostId}")
    public String enviar(@PathVariable("propostaId") long propostaId, Model model) throws Exception {

        Proposta proposta = propostaService.get(propostaId);

        if (proposta == null) {
            throw new Exception("A proposta não existe");
        }

        // If user is NOT owner or contractor, he can't post:
        if (!PossoPostar(proposta)) {
            throw new Exception("Not owner of the job nor contractor!");
        }

        Feedback feedback = feedbackService.findByBid(bid);

        if (alreadySentFeedback(feedback)) {
            return "redirect:/feedback/view/" + bid.getId();
        }

        String myRoleForBid = getMyRole(bid);
        String cooperatorName = myRoleForBid.equals(ROLE_CLIENT) ? bid.getJob().getAuthor().getName()
                : bid.getUser().getName();

        model.addAttribute("bid", bid);
        model.addAttribute("cooperator_name", cooperatorName);

        return "frontend/feedback/send";
    }

    @PostMapping("/save")
    public String save(@RequestParam(name = "bid_id", required = true) Long bidId,
            @RequestParam(name = "rate", required = true) int rate,
            @RequestParam(name = "feedback", required = true) String feedbackText) throws Exception {

        if (rate < 1 || rate > 5) {
            throw new Exception("Rate must be between 1 and 5");
        }
        if (feedbackText.length() < 5) {
            throw new Exception("Please enter feedback text");
        }

        Bid bid = bidService.get(bidId);

        if (!iCanPost(bid)) {
            throw new Exception("Not owner of the job nor contractor!");
        }

        Feedback feedback = new Feedback();
        feedback.setBid(bid);

        String myRoleForBid = getMyRole(bid);

        // If I am contractor (owner of the bid), set client rate&feedback.
        // Otherwise, I am job owner and I set contractor rate&feedback.
        if (myRoleForBid.equals(ROLE_CONTR)) {
            feedback.setClientFeedback(feedbackText);
            feedback.setClientRate(rate);
        } else {
            feedback.setContractorFeedback(feedbackText);
            feedback.setContractorRate(rate);
        }

        // If feedback for the bid exists, update it. Otherwise, insert new.
        Feedback dbFeedback = feedbackService.findByBid(bid);
        if (dbFeedback == null) {
            dbFeedback = feedbackService.save(feedback); // Insert
        } else {

            if (myRoleForBid.equals(ROLE_CONTR)) {
                dbFeedback.setClientFeedback(feedback.getClientFeedback());
                dbFeedback.setClientRate(feedback.getClientRate());
            } else {
                dbFeedback.setContractorFeedback(feedback.getContractorFeedback());
                dbFeedback.setContractorRate(feedback.getContractorRate());
            }

            dbFeedback = feedbackService.save(dbFeedback); // Update

        }

        return "redirect:/feedback/view/" + bid.getId();
    }

    @GetMapping("view/{bidId}")
    public String viewBidFeedbacks(@PathVariable("bidId") long bidId, Model model) throws Exception {

        Bid bid = bidService.get(bidId);

        if (!iCanPost(bid)) {
            throw new Exception("Not owner of the job nor contractor!");
        }

        Feedback feedback = feedbackService.findByBid(bid);

        model.addAttribute("bid", bid);
        model.addAttribute("feedback", feedback);
        model.addAttribute("can_post_feedback", !alreadySentFeedback(feedback) && iCanPost(bid));

        return "frontend/feedback/view";

    }

    private boolean PossoPostar(Proposta proposta) {
        Usuario eu = getUsuarioAtual();
        return eu.getId() != proposta.getUsuario().getId() || eu.getId() != proposta.getTrabalho().getAutor().getId();
    }

    private boolean alreadySentFeedback(Feedback feedback) {
        if (feedback == null) {
            return false;
        }
        String myRole = getMyRole(feedback.getBid());

        return (myRole.equals(ROLE_CONTR) && feedback.getClientRate() > 0)
                || (myRole.equals(ROLE_CLIENT) && feedback.getContractorRate() > 0);
    }

    private String getMyRole(Bid bid) {
        return getCurrentUser().getId().equals(bid.getUser().getId()) ? ROLE_CONTR : ROLE_CLIENT;
    }

}
