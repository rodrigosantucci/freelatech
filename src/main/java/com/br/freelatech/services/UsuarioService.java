package com.br.freelatech.services;


import com.br.freelatech.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.br.freelatech.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public Usuario get(Long usuario_id) {
		return usuarioRepository.findOne(usuario_id);
	}
	
	
	public Usuario getByEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}
	
	
	public Usuario save(Usuario usuario) {
		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		return usuarioRepository.save(usuario);
	}
}
