package com.br.freelatech.controllers;

import java.util.Locale;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@EnableAutoConfiguration
@Controller
@RequestMapping("/")
public class InicioController {
    @GetMapping
    public String inicio(Model model, Locale loc){
        return "inicio";
    }    
}
