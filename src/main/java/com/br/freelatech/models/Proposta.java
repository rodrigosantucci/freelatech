package com.br.freelatech.models;

import javax.persistence.*;

@Entity
@Table(name = "tb_proposta")
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double preco;

    @Column(length = 255)
    private String prazo;

    private String dataCriacao;

    @Column(length = 64000)
    private String proposta_texto;

    @Column(name = "aceita")
    private int propostaAceita = 0;

    @Column(name = "fechado")
    private int fechado = 0;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    private Trabalho trabalho;

    public void setId(Long id) {
        this.id = id;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
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

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setData_criacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getProposta_texto() {
        return proposta_texto;
    }

    public void setProposta_texto(String proposta_texto) {
        this.proposta_texto = proposta_texto;
    }

    public int getPropostaAceita() {
        return propostaAceita;
    }

    public void setPropostaAceita(int propostaAceita) {
        this.propostaAceita = propostaAceita;
    }

    public int getFechado() {
        return fechado;
    }

    public void setFechado(int fechado) {
        this.fechado = fechado;
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

    public Long getId() {
        return id;
    }
}
