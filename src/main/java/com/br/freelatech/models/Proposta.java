package com.br.freelatech.models;

import java.util.Date;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "tb_proposta")
public class Proposta{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer proposta_id;
    private Double preco;

    @Column(length = 255)
    private String prazo;

    private Date dataCriacao;

    @Column(length = 64000)
    private String proposta;
    
    @Column(name = "aceita")
    private int propostaAceita = 0;
    
    @Column(name = "fechado")
    private int statusProposta = 0;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    private Trabalho trabalho;


    public Integer getProposta_id() {
        return proposta_id;
    }

    public void setProposta_id(Integer proposta_id) {
        this.proposta_id = proposta_id;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getPrazo() {
        return prazo;
    }

    public void setPrazo(String prazo) {
        this.prazo = prazo;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setData_criacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getProposta() {
        return proposta;
    }

    public void setProposta(String proposta) {
        this.proposta = proposta;
    }

    public int getPropostaAceita() {
		return propostaAceita;
	}

	public void setPropostaAceita(int propostaAceita) {
		this.propostaAceita = propostaAceita;
	}

	public int getStatusProposta() {
		return statusProposta;
	}

	public void setStatusProposta(int statusProposta) {
		this.statusProposta = statusProposta;
	}

	public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Trabalho getTrabalho() {
        return trabalho;
    }

    public void setTrabalho(Trabalho trabalho) {
        this.trabalho = trabalho;
    }
}
