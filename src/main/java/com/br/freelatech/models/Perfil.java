package com.br.freelatech.models;


import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Perfil {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long perfil_id;
	
	private String biografia;
	private String social;
	private String localizacao;
	
	@OneToOne
	@JsonBackReference
	private Usuario usuario;

	public Long getPerfil_id() {
		return perfil_id;
	}

	public void setPerfil_id(Long perfil_id) {
		this.perfil_id = perfil_id;
	}

	public String getBiografia() {
		return biografia;
	}

	public void setBiografia(String biografia) {
		this.biografia = biografia;
	}

	public String getSocial() {
		return social;
	}

	public void setSocial(String social) {
		this.social = social;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	

}
