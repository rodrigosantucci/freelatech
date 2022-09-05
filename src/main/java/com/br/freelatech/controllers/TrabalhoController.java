package com.br.freelatech.controllers;

import com.br.freelatech.models.Usuario;
import com.br.freelatech.models.Trabalho;
import com.br.freelatech.models.Feedback;
import com.br.freelatech.models.Proposta;
import com.br.freelatech.services.PropostaService;
import com.br.freelatech.services.CategoriaService;
import com.br.freelatech.services.FeedbackService;
// import com.br.freelatech.services.FeedbackService;
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
public class TrabalhoController extends AbstratoController{

    @Autowired
    TrabalhoService trabalhoService;

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    PropostaService propostaService;
    
    @Autowired
    FeedbackService feedbackService;
    
    @Value( "${freelancer.job.page_size}" )
    private int jobPageSize;
    
    
    @GetMapping
    public String listarTrabalhos(Model model, HttpServletRequest request){
    	
    	String pageUrl = "/trabalho?a=a"; 
    	
    	String filt = request.getParameter("filter");
    	String pPage = request.getParameter("page");
    	
    	Usuario eu = getUsuarioAtual();
    	boolean isMyJobsPage = false;
    	
    	Map<String, Object> filter = new HashMap<>();

    	if( filt != null && filt.equals("myjobs") && eu != null) {
    		
    		filter.put("usuario", eu );
    		pageUrl = "/trabalho?filter=myjobs";
            isMyJobsPage = true;
    	
    	}
    	
    	int pageNo = 1;
    	if( pPage != null ) {
    		pageNo = Integer.parseInt(pPage);
    	}
    	
    	Page<Trabalho> jobsPage = trabalhoService.findAllPaged(filter, pageNo, jobPageSize);
    	
    	model.addAttribute("is_my_jobs_page", isMyJobsPage);
    	model.addAttribute("jobs_page", jobsPage);
    	model.addAttribute("page_url", pageUrl);
    	
        return "trabalho/trabalhos";
    }

    @GetMapping({"/ver/{id}", "/{id}}" })
    public String viewJob(Model model, @PathVariable("id") long id){

        Trabalho trabalho = trabalhoService.get(id);

        model.addAttribute("trabalho", trabalho);

        // Get my bid for the job and assign to the view
        Proposta minhaProposta = null;

        // Check if logged in:
        Usuario currentUser = super.getUsuarioAtual();
        if( currentUser != null){
            minhaProposta = propostaService.getUsuarioPropostaPorTrabalho(currentUser, trabalho);
            if(minhaProposta != null) {
	        	// New line to <br>
	            minhaProposta.setProposta(FreelatechHelper.nl2br(minhaProposta.getProposta()));
            }
        }

        model.addAttribute("minhaProposta", minhaProposta);
        
        model.addAttribute("eu", getUsuarioAtual());

        // Calcular cliente nota:
        long avgClienteFeedback = 0;
		int totalFeedbackNo = 0;
        List<Feedback> feedbacks = feedbackService.findByCliente(trabalho.getAutor());
        if(feedbacks.size() > 0) {
			int sum = 0;
			int no = 0;
			for (Feedback f : feedbacks) {
				sum += f.getClienteAvaliacao();
				no++;
			}
			avgClienteFeedback = sum / no;
			totalFeedbackNo = feedbacks.size();
        }
		// Calcular contratante nota:
		List<Trabalho> trabalhos = trabalhoService.findByAutor(trabalho.getAutor());
		List<Trabalho> contratadosTrabalho = trabalhoService.findContratadoTrabalhosByAutor(trabalho.getAutor());
		double totalJobsNo = trabalhos.size();
		double hiredJobsNo = contratadosTrabalho.size();
		double hireRate = (hiredJobsNo / totalJobsNo) * 100; // percent

		model.addAttribute("average_client_feedback_rate", avgClienteFeedback);
		model.addAttribute("reviews_no", totalFeedbackNo);
		model.addAttribute("bids_no", propostaService.findByTrabalho(trabalho).size());
		model.addAttribute("hire_rate", (int) hireRate);
		model.addAttribute("jobs_no", (int) totalJobsNo);
		model.addAttribute("hired_jobs_no", (int) hiredJobsNo);

		return "frontend/job/view_job";
    }

    @GetMapping("/criar")
    public String createJob(Model model){
        model.addAttribute("categories", categoriaService.list());
        return "trabalho/criar_trabalho";
    }

    @PostMapping("/salvar")
    public String salvarTrabalho(
            @RequestParam(name = "id", required = false) Long id,
            @ModelAttribute Trabalho trabalho,
            Model model){

        if(trabalho.getTitulo().isEmpty() /*|| 1==1*/){
            model.addAttribute("error", "É necessário preencher o campo título");
            return "trabalho/criar_trabalho";
        }

        // Set current loged user ID as author
        Usuario autor = super.getUsuarioAtual();
        if(autor == null){
            System.out.println("É necessário fazer login!");
            return null;
        }
        trabalho.setAutor(autor);

        Trabalho trabalhoSalvo = null;
        if( id != null && id > 0){
// TODO
        } else {
            trabalhoSalvo = trabalhoService.add(trabalho);
        }
        return "redirect:/trabalho/ver/" + trabalhoSalvo.getTrabalho_id();
    }

    @GetMapping("/propostas/{trabalhoId}")
    public String viewBids(Model model, @PathVariable("trabalhoId") long trabalhoId) {
    	
    	Trabalho trabalho = trabalhoService.get(trabalhoId);
    	
    	Usuario eu = getUsuarioAtual();
    	
    	if( trabalho == null || trabalho.getAutor().getUsuario_id() != eu.getUsuario_id() ) {
    		System.out.println("Nenhum trabalho encontrado");
    		return "redirect:/trabalho/ver/" + trabalhoId;
    	}
    	
    	List<Proposta> propostas = propostaService.findByTrabalho(trabalho);
    	
    	model.addAttribute("trabalho", trabalho);
    	model.addAttribute("propostas", propostas);
    	
    	// If me != author
    	return "trabalho/ver_propostas";
    }

   

}
