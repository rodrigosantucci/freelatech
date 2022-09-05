package com.br.freelatech.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedback_id;

    @ManyToOne
    @JoinColumn(name = "proposta_id", nullable = true)
    private Proposta proposta;

    private Integer clienteAvaliacao;
    private String clienteFeedback;

    private Integer contratanteAvaliacao;
    private String contratanteFeedback;

    
    public Long getFeedback_id() {
        return feedback_id;
    }
    public Proposta getProposta() {
        return proposta;
    }
    public Integer getClienteAvaliacao() {
        return clienteAvaliacao;
    }
    public String getClienteFeedback() {
        return clienteFeedback;
    }
    public Integer getContratanteAvaliacao() {
        return contratanteAvaliacao;
    }
    public String getContratanteFeedback() {
        return contratanteFeedback;
    }
    public void setFeedback_id(Long feedback_id) {
        this.feedback_id = feedback_id;
    }
    public void setProposta(Proposta proposta) {
        this.proposta = proposta;
    }
    public void setClienteAvaliacao(Integer clienteAvaliacao) {
        this.clienteAvaliacao = clienteAvaliacao;
    }
    public void setClienteFeedback(String clienteFeedback) {
        this.clienteFeedback = clienteFeedback;
    }
    public void setContratanteAvaliacao(Integer contratanteAvaliacao) {
        this.contratanteAvaliacao = contratanteAvaliacao;
    }
    public void setContratanteFeedback(String contratanteFeedback) {
        this.contratanteFeedback = contratanteFeedback;
    }


    
}
