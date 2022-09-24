package com.br.freelatech.services;

import com.br.freelatech.models.Mensagem;
import com.br.freelatech.models.Usuario;
import com.br.freelatech.models.Trabalho;
import com.br.freelatech.repositories.MensagemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MensagemService {

    @Autowired
    MensagemRepository mensagemRepository;

    @Value("${freelatech.sala_mensagem.pagina_tamanho}")
    private int salaMensagemPaginaTamanho;

    public Mensagem salvar(Mensagem mensagem) {
        return mensagemRepository.save(mensagem);
    }

    public Mensagem get(Long id) {
        return mensagemRepository.findOne(id);
    }

    public List<Mensagem> findByTrabalhoAndContratante(Trabalho trabalho, Usuario contratante) {

        int numeroPagina = 1;

        PageRequest request = new PageRequest(numeroPagina - 1, salaMensagemPaginaTamanho, Sort.Direction.DESC, "id");

        Page<Mensagem> messages = mensagemRepository.findByTrabalhoAndRemetenteOuDestinatario(trabalho, contratante,
                request);
        return messages.getContent();

    }

    public List<Mensagem> getSalasPorUsuario(Usuario eu) {
        List<Mensagem> todasMensagens = mensagemRepository.findByRemetenteOuDestinatario(eu);
        List<Mensagem> resultado = new ArrayList<Mensagem>();

        Map<String, Mensagem> salasUnicas = new HashMap<String, Mensagem>();

        todasMensagens.forEach(m -> {

            // Unique hash map key "trabalho-contributor"
            String key = m.getTrabalho() != null ? String.valueOf(m.getTrabalho().getId()) : "X";
            key += '-';
            key += (m.getDestinatario().getId() == eu.getId() ? m.getRemetente().getId() : m.getDestinatario().getId());

            // Se a sala nao existir, adiciona na listagem unica
            Mensagem m2 = salasUnicas.get(key);
            if (m2 == null) {
                salasUnicas.put(key, m);
            }

        });

        for (String t_key : salasUnicas.keySet()) {
            resultado.add(salasUnicas.get(t_key));
        }

        // retorna todas as mensagens;
        return resultado;
    }

    public List<Mensagem> findByMeuInterador(Usuario eu, Usuario interador) {
        return mensagemRepository.findByMeuInterador(eu, interador);
    }

}
