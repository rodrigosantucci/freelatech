package com.br.freelatech.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserDetailsImpl extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;

	String nomeCompleto;

	public UserDetailsImpl(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

}
