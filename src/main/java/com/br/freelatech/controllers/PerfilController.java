package com.br.freelatech.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.br.freelatech.models.Proposta;
import com.br.freelatech.models.Feedback;
import com.br.freelatech.models.Trabalho;
import com.br.freelatech.models.TrabalhoHistorico;
import com.br.freelatech.models.Perfil;
import com.br.freelatech.models.Usuario;
import com.br.freelatech.services.UsuarioService;
import com.br.freelatech.services.PropostaService;
import com.br.freelatech.services.FeedbackService;
import com.br.freelatech.services.TrabalhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/perfil")
public class PerfilController extends AbstratoController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    PropostaService propostaService;

    @Autowired
    TrabalhoService trabalhoService;

    @Autowired
    FeedbackService feedbackService;

    @RequestMapping(value = { "", "/{id}" })
    public String verPerfil(@PathVariable("id") Optional<Long> perfilIdParam, Model model) {

        Long usuarioId = perfilIdParam.isPresent() ? perfilIdParam.get() : 0L;

        Usuario usuarioLogado = super.getUsuarioAtual();

        if (usuarioLogado == null) {
            return "redirect:/";
        }

        Usuario usuario;
        boolean editavel = false;

        // If profile ID is not provided in URL, fetch currently logged user
        if (usuarioId < 1) {
            usuario = usuarioLogado;
            editavel = true;
        } else {
            usuario = usuarioService.get(usuarioId);
            if (usuarioId == usuarioLogado.getId()) {
                editavel = true;
            }
        }

        if (usuario == null) {
            return "redirect:/";
        }

        List<Proposta> minhasPropostas = null;
        if (editavel) {
            minhasPropostas = propostaService.findByUsuario(usuario);
        }

        List<Proposta> propostasFechadas = propostaService.findByFechadoAndUsuario(1, usuario);

        List<Feedback> meusFeedbacks = feedbackService.findByPropostas(propostasFechadas);

        model.addAttribute("usuario", usuario);
        model.addAttribute("perfil", usuario.getPerfil());
        model.addAttribute("Editavel", editavel);
        model.addAttribute("minhasPropostas", minhasPropostas);
        model.addAttribute("meusFeedbacks", meusFeedbacks);

        return "perfil/ver_perfil";
    }

    @GetMapping("/editar")
    public String editarPerfil(Model model) {

        Usuario usuario = getUsuarioAtual();
        model.addAttribute("usuario", usuario);

        return "perfil/editar_perfil";
    }

    @PostMapping("/salvar")
    public String salvarPerfil(@ModelAttribute @Valid Usuario usuario, @ModelAttribute Perfil perfil, Model model) {

        //
        // Get and update current logged user. Don't use params from input to prevent
        // unauthorized editing.
        Usuario eu = getUsuarioAtual();
        eu.setNome(usuario.getNome());
        eu.setEmail(usuario.getEmail());

        if (eu.getPerfil() != null) {
            eu.getPerfil().setSocial(perfil.getSocial());
            eu.getPerfil().setLocalizacao(perfil.getLocalizacao());
            eu.getPerfil().setBiografia(perfil.getBiografia());
        } else {
            eu.setPerfil(perfil);
            eu.getPerfil().setUsuario(eu);
        }

        usuarioService.save(eu);

        return "redirect:/perfil";
    }

    @GetMapping("/cliente/{id}")
    public String verClientePerfil(@PathVariable("id") Long usuarioId, Model model) throws Exception {

        Usuario usuario = usuarioService.get(usuarioId);
        if (usuario == null) {
            throw new Exception("Usuario não encontrado.");
        }

        List<Trabalho> clienteTrabalhos = trabalhoService.findByAutor(usuario);

        List<TrabalhoHistorico> trabalhoHistorico = new ArrayList<>();

        int totalTrabalhos = 0;
        int trabalhosContradados = trabalhoService.findContratadoTrabalhosByAutor(usuario).size();

        for (Trabalho t : clienteTrabalhos) {

            totalTrabalhos++;

            TrabalhoHistorico trHistorico = new TrabalhoHistorico();
            Feedback feedback = feedbackService.findByTrabalho(t);

            trHistorico.setTrabalho(t);
            trHistorico.setFeedback(feedback);
            trabalhoHistorico.add(trHistorico);

        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("perfil", usuario.getPerfil());
        model.addAttribute("totalTrabalhos", totalTrabalhos);
        model.addAttribute("trabalhosContratados", trabalhosContradados);
        model.addAttribute("trabalhoHistorico", trabalhoHistorico);

        return "perfil/ver_cliente_perfil";

    }

}
