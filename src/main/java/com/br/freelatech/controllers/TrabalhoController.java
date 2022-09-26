package com.br.freelatech.controllers;

import com.br.freelatech.models.Usuario;
import com.br.freelatech.models.Trabalho;
import com.br.freelatech.models.Feedback;
import com.br.freelatech.models.Proposta;
import com.br.freelatech.services.PropostaService;
import com.br.freelatech.services.CategoriaService;
import com.br.freelatech.services.FeedbackService;
import com.br.freelatech.services.TrabalhoService;
import com.br.freelatech.util.FreelatechHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/trabalho")
public class TrabalhoController extends AbstratoController {

    @Autowired
    TrabalhoService trabalhoService;

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    PropostaService propostaService;

    @Autowired
    FeedbackService feedbackService;

    @Value("${freelatech.trabalho.page_size}")
    private int jobPageSize;

    @GetMapping
    public String listarTrabalhos(Model model, HttpServletRequest request) {

        String paginaUrl = "/trabalho?a=a";

        String filt = request.getParameter("filtro");
        String pPage = request.getParameter("pagina");

        Usuario eu = getUsuarioAtual();
        boolean isMytrabalhosPagina = false;

        Map<String, Object> filter = new HashMap<>();

        if (filt != null && filt.equals("meusTrabalhos") && eu != null) {

            filter.put("usuario", eu);
            paginaUrl = "/trabalho?filtro=meusTrabalhos";
            isMytrabalhosPagina = true;

        }

        int pageNo = 1;
        if (pPage != null) {
            pageNo = Integer.parseInt(pPage);
        }

        Page<Trabalho> trabalhosPagina = trabalhoService.findAllPaged(filter, pageNo, jobPageSize);

        model.addAttribute("is_my_trabalhos_pagina", isMytrabalhosPagina);
        model.addAttribute("trabalhos_pagina", trabalhosPagina);
        model.addAttribute("pagina_url", paginaUrl);

        return "trabalho/trabalhos";

    }

    @GetMapping({ "/ver/{id}", "/{id}}" })
    public String verTrabalho(Model model, @PathVariable("id") long id) {

        Trabalho trabalho = trabalhoService.get(id);

        model.addAttribute("trabalho", trabalho);

        // Get my bid for the job and assign to the view
        Proposta minhaProposta = null;

        // Check if logged in:
        Usuario usuarioAtual = super.getUsuarioAtual();
        if (usuarioAtual != null) {
            minhaProposta = propostaService.getUsuarioPropostaByTrabalho(usuarioAtual, trabalho);
            if (minhaProposta != null) {
                // New line to <br>
                minhaProposta.setProposta_texto(FreelatechHelper.nl2br(minhaProposta.getProposta_texto()));
            }
        }

        model.addAttribute("minhaProposta", minhaProposta);

        model.addAttribute("eu", getUsuarioAtual());

        // Calcular cliente nota:
        long avgClienteFeedback = 0;
        int totalFeedbackNo = 0;
        List<Feedback> feedbacks = feedbackService.findByCliente(trabalho.getAutor());
        if (feedbacks.size() > 0) {
            int sum = 0;
            int no = 0;
            for (Feedback f : feedbacks) {
                if (f.getClienteAvaliacao() != null) {
                    sum += f.getClienteAvaliacao();
                } else {
                    sum += 0;
                }

                no++;
            }
            avgClienteFeedback = sum / no;
            totalFeedbackNo = feedbacks.size();
        }
        // Calcular contratante nota:
        List<Trabalho> trabalhos = trabalhoService.findByAutor(trabalho.getAutor());
        List<Trabalho> contratadosTrabalho = trabalhoService.findContratadoTrabalhosByAutor(trabalho.getAutor());
        double totalTrabalhosNo = trabalhos.size();
        double trabalho_contratado_no = contratadosTrabalho.size();
        double taxa_contratacao = (trabalho_contratado_no / totalTrabalhosNo) * 100; // percent

        model.addAttribute("avgClienteFeedback", avgClienteFeedback);
        model.addAttribute("feedbacks_no", totalFeedbackNo);
        model.addAttribute("propostas_no", propostaService.findByTrabalho(trabalho).size());
        model.addAttribute("taxa_contratacao", (int) taxa_contratacao);
        model.addAttribute("trabalhos_no", (int) totalTrabalhosNo);
        model.addAttribute("trabalho_contratado_no", (int) trabalho_contratado_no);

        return "trabalho/ver_trabalho";
    }

    @GetMapping("/criar")
    public String criarTrabalho(Model model) {
        model.addAttribute("categorias", categoriaService.list());
        return "trabalho/criar_trabalho";
    }

    @PostMapping("/salvar")
    public String salvarTrabalho(
            @RequestParam(name = "id", required = false) Long id,
            @ModelAttribute Trabalho trabalho,
            Model model) {

        if (trabalho.getTitulo().isEmpty() /* || 1==1 */) {
            model.addAttribute("error", "É necessário preencher o campo título");
            return "trabalho/criar_trabalho";
        }

        // Set current loged user ID as author
        Usuario autor = super.getUsuarioAtual();
        if (autor == null) {
            System.out.println("É necessário fazer login!");
            return null;
        }
        trabalho.setAutor(autor);
        String data_criacao = FreelatechHelper.getCurrentMySQLDate();
        trabalho.setData_criacao(data_criacao);

        Trabalho trabalhoSalvo = null;
        if (id != null && id > 0) {
            // TODO
        } else {

            trabalhoSalvo = trabalhoService.add(trabalho);

        }
        return "redirect:/trabalho/ver/" + trabalhoSalvo.getId();
    }

    @GetMapping("/propostas/{trabalhoId}")
    public String verPropostas(Model model, @PathVariable("trabalhoId") long trabalhoId) {

        Trabalho trabalho = trabalhoService.get(trabalhoId);

        Usuario eu = getUsuarioAtual();

        if (trabalho == null || trabalho.getAutor().getId() != eu.getId()) {
            System.out.println("Nenhum trabalho encontrado");
            return "redirect:/trabalho/ver/" + trabalhoId;
        }

        List<Proposta> propostas = propostaService.findByTrabalho(trabalho);

        model.addAttribute("trabalho", trabalho);
        model.addAttribute("propostas", propostas);

        // If eu != author
        return "trabalho/ver_propostas";
    }

}
