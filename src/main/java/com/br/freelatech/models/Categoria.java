package com.br.freelatech.models;

import java.io.Serializable;

import javax.persistence.*;


@Entity
@Table(name = "tb_categoria")
public class Categoria  {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer categoria_id;
	
	private String nome;

	public Integer getCategoria_id() {
		return categoria_id;
	}

	public void setCategoria_id(Integer categoria_id) {
		this.categoria_id = categoria_id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
}
