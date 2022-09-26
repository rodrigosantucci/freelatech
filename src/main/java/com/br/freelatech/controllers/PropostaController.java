package com.br.freelatech.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.br.freelatech.models.Proposta;
import com.br.freelatech.models.Trabalho;
import com.br.freelatech.models.Usuario;
import com.br.freelatech.services.PropostaService;
import com.br.freelatech.services.TrabalhoService;
import com.br.freelatech.util.FreelatechHelper;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

@Controller
@RequestMapping("/proposta")
public class PropostaController extends AbstratoController {

    @Autowired
    PropostaService propostaService;

    @Autowired
    TrabalhoService trabalhoService;

    @PostMapping("/salvar")
    public String salvarProposta(@Valid Proposta proposta, @RequestParam("trabalho_id") Long trabalhoId) {

        Usuario usuario = super.getUsuarioAtual();

        if (usuario == null) {
            return "redirect:/";
        }

        Trabalho trabalho = trabalhoService.get(trabalhoId);
        String dataCriacao = FreelatechHelper.getCurrentMySQLDate();
        proposta.setTrabalho(trabalho);
        proposta.setUsuario(usuario);
        proposta.setDataCriacao(dataCriacao);

        propostaService.salvar(proposta);
        return "redirect:/trabalho/ver/" + trabalho.getId();
    }

    @GetMapping("/aceito/{propostaId}")
    public String aceitarProposta(Model model, @PathVariable("propostaId") long propostaId) throws Exception {

        Proposta proposta = propostaService.get(propostaId);
        Usuario eu = getUsuarioAtual();

        if (proposta == null || eu.getId() != proposta.getTrabalho().getAutor().getId()) {

            throw new Exception("Não existe uma proposta feita pelo usuario.");
        }

        boolean salvo = propostaService.aceitaProposta(proposta);

        if (!salvo) {
            throw new Exception("Proposta não pode ser salva!");
        }

        return "redirect:/mensagem/trabalho_sala/" + proposta.getTrabalho().getId() + "/"
                + proposta.getUsuario().getId();
    }

    @GetMapping("/meus-contratos")
    public String meusContratos(Model model) throws Exception {

        Usuario eu = getUsuarioAtual();

        Set<Proposta> contratos = new HashSet<Proposta>(propostaService.findByUsuario(eu));

        List<Proposta> propostasParaMeusTrabalhos = propostaService.findByUsuarioTrabalhos(eu);

        contratos.addAll(propostasParaMeusTrabalhos);
        contratos.removeIf(proposta -> {
            return proposta.getPropostaAceita() == 0;
        });

        model.addAttribute("contratos", contratos);
        model.addAttribute("eu", eu);

        return "proposta/meus_contratos";

    }

    @GetMapping("fechar/{propostaId}")
    public String fechar(Model model, @PathVariable("propostaId") long propostaId) throws Exception {

        Usuario eu = getUsuarioAtual();
        Proposta proposta = propostaService.get(propostaId);

        if (proposta.getFechado() == 1) {
            return "redirect:/feedback/ver/" + propostaId;
        }

        if (proposta.getTrabalho().getAutor().getId() != eu.getId()) {
            throw new Exception("Você não pode fechar essa proposta porque não é o dono do serviço!");
        }

        proposta.setFechado(1);

        propostaService.salvar(proposta);

        return "redirect:/feedback/" + propostaId;
    }

}
