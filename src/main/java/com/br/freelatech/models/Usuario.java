package com.br.freelatech.models;


import org.hibernate.validator.constraints.*;
import javax.persistence.*;



@Entity
@Table(name = "tb_usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Long id;

	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	private String nome;
	

	@Column(unique = true)
	@Email
	private String email;
	
	@Column(nullable = false,
			updatable = false)
	private String senha;
	private String data_criacao;
	
	@OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
	private Perfil perfil;
	
	
	
	
	
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getData_criacao() {
		return data_criacao;
	}
	public void setData_criacao(String data_criacao) {
		this.data_criacao = data_criacao;
	}
	public Perfil getPerfil() {
		return perfil;
	}
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
		
}
