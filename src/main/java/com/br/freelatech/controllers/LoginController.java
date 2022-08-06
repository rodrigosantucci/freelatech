package com.br.freelatech.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import com.br.freelatech.models.Usuario;
import com.br.freelatech.services.UsuarioService;




@Controller
public class LoginController {

	
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	@Qualifier("usuarioValidator")
	Validator usuarioValidator;
	
	
	
	
	@GetMapping("/login")
	public String login() {
		return "login/login";
	}
	
	@RequestMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "login/login";
	}
	
	@GetMapping("/cadastro")
	public String cadastro() {
		return "login/cadastro";
	}
	
	
	
	@PostMapping("/cadastro")
	public ModelAndView Cadastrar(@Valid Usuario usuario, BindingResult bindingResult, Model model) {
		
		usuarioValidator.validate(usuario, bindingResult);
		
		if( bindingResult.hasErrors() ) {
				StringBuilder sb = new StringBuilder();
				bindingResult.getAllErrors().forEach(e -> {
				
				sb.append(StringUtils.capitalize(e.getDefaultMessage()));
				sb.append("<br>");
				
			});
			
				model.addAttribute("error", sb);
			} else {
				
				usuarioService.save(usuario);
				return new ModelAndView("redirect:/login");
			}
		
			model.addAttribute("usuario", usuario);
			return new ModelAndView("login/cadastrar", model.asMap());
		}
		
		
	} 
	

