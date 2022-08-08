package com.br.freelatech.models;

import javax.persistence.*;


@Entity
@Table(name = "tb_proposta")
public class Proposta{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(columnDefinition = "INTEGER(11)") // Fix of org.hibernate.tool.schema.spi.SchemaManagementException
    private Long id;
    private Double preco;

    @Column(length = 255)
    private String prazo;

    private String dataCriacao;

    @Column(length = 64000)
    private String proposta;
    
    private int propostaAceita = 0;
    
    private int statusProposta = 0;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    private Trabalho trabalho;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
