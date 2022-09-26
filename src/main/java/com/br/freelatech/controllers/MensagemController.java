package com.br.freelatech.controllers;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.freelatech.models.Proposta;
import com.br.freelatech.models.Trabalho;
import com.br.freelatech.models.Mensagem;
import com.br.freelatech.models.Usuario;
import com.br.freelatech.services.PropostaService;
import com.br.freelatech.services.TrabalhoService;
import com.br.freelatech.services.MensagemService;
import com.br.freelatech.services.UsuarioService;

@Controller
@RequestMapping("/mensagem")
public class MensagemController extends AbstratoController {

    @Autowired
    MensagemService mensagemService;

    @Autowired
    TrabalhoService trabalhoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    PropostaService propostaService;

    @Value("${freelatech.sala_mensagem.pagina_tamanho}")
    private int salaMensagemPaginaTamanho;

    @GetMapping
    public String minhaSalaMensagem(Model model) {

        Usuario eu = getUsuarioAtual();
        List<Mensagem> mensagens = mensagemService.getSalasPorUsuario(eu);

        model.addAttribute("mensagens", mensagens);

        model.addAttribute("meu_id", eu.getId());

        return "/mensagem/minha_sala_mensagem";
    }

    @GetMapping("/trabalho_sala/{trabalhoId}/{contratante}")
    public String trabalhoSala(Model model,
            @PathVariable("trabalhoId") long trabalhoId,
            @PathVariable("contratante") long contratanteId) {

        Usuario eu = getUsuarioAtual();

        Trabalho trabalho = trabalhoService.get(trabalhoId);

        Usuario contratante = usuarioService.get(contratanteId);

        String contatoUrl = "/perfil/" + contratante.getId();

        Proposta proposta = null;
        List<Mensagem> mensagens = null;
        if (trabalho != null) {
            proposta = trabalho.getAutor().getId() == eu.getId()
                    ? propostaService.getUsuarioPropostaByTrabalho(contratante, trabalho)
                    : propostaService.getUsuarioPropostaByTrabalho(eu, trabalho);

            if (trabalho.getAutor().getId() != eu.getId()) {
                contatoUrl = "/perfil/cliente/" + contratante.getId();
            }

            mensagens = mensagemService.findByTrabalhoAndContratante(trabalho, contratante);
            // messages.sort( (Mensagem o1, Mensagem o2) -> {
            // return o2.getId().compareTo(o1.getId());
            // });
        } else {
            mensagens = mensagemService.findByMeuInterador(eu, contratante);
        }

        model.addAttribute("trabalho", trabalho);
        model.addAttribute("contato", contratante);
        model.addAttribute("contato_url", contatoUrl);
        model.addAttribute("proposta", proposta);
        model.addAttribute("mensagens", mensagens);
        model.addAttribute("sala_mensagem.pagina_tamanho", salaMensagemPaginaTamanho);
        model.addAttribute("eu", eu);

        return "/mensagem/trabalho_sala";
    }

    @PostMapping("/trabalho_sala/{trabalhoId}/{contratante}")
    public String enviarMensagemParaSalaTrabalho(
            HttpServletRequest request,
            @PathVariable("trabalhoId") long trabalhoId,
            @PathVariable("contratante") long contratanteId) throws Exception {

        Trabalho trabalho = trabalhoService.get(trabalhoId);
        Usuario contratante = usuarioService.get(contratanteId);
        Usuario eu = getUsuarioAtual();

        // Check if I have rights to add mensagem:
        if (trabalho != null) {
            if (trabalho.getAutor().getId() != eu.getId() && trabalho.getAutor().getId() != contratante.getId()) {
                throw new Exception("Usuario atual não pode enviar mensagem para esse trabalho");
            }
        }

        String mensagemTexto = request.getParameter("mensagem");

        Mensagem mensagem = new Mensagem();
        if (trabalho != null) {
            mensagem.setTrabalho(trabalho);
        }
        mensagem.setDestinatario(contratante);
        mensagem.setRemetente(getUsuarioAtual());
        mensagem.setTexto(mensagemTexto);
        mensagem.setDataEnvio(new Date());

        Mensagem resultado = mensagemService.salvar(mensagem);
        if (resultado == null) {
            throw new Exception("Não foi possível salvar nova mensagem");
        }

        return "redirect:" + request.getHeader("Referer");

    }
}
