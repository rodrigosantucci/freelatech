package com.br.freelatech.models;

import javax.persistence.*;

@Entity
public class Trabalho {
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long trabalho_id;
	
	private String titulo;
	private String descricao;
	private Double orcamento;
	private String tipo;
	private String experiencia;
	private String data_criacao;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "autor_id")
	private Usuario autor;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "categoria_id")
	private Categoria categoria;

	public long getTrabalho_id() {
		return trabalho_id;
	}

	public void setTrabalho_id(long trabalho_id) {
		this.trabalho_id = trabalho_id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Double orcamento) {
		this.orcamento = orcamento;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getExperiencia() {
		return experiencia;
	}

	public void setExperiencia(String experiencia) {
		this.experiencia = experiencia;
	}

	public String getData_criacao() {
		return data_criacao;
	}

	public void setData_criacao(String data_criacao) {
		this.data_criacao = data_criacao;
	}

	public Usuario getAutor() {
		return autor;
	}

	public void setAutor(Usuario autor) {
		this.autor = autor;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	

	
}
