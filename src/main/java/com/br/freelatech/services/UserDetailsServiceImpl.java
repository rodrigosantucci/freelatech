package com.br.freelatech.services;

import com.br.freelatech.models.Usuario;
import com.br.freelatech.repositories.UsuarioRepository;
import com.br.freelatech.security.UserDetailsImpl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = usuarioRepository.findByEmail(username);

		if (usuario == null) {
			throw new UsernameNotFoundException(
					"Não foi encontrado nenhum usuário com esse nome de usuário: " + username);
		}

		Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
		grantedAuthorities.add(new SimpleGrantedAuthority("usuario"));

		UserDetailsImpl userDetails = new UserDetailsImpl(usuario.getEmail(), usuario.getSenha(),
				grantedAuthorities);
		userDetails.setNomeCompleto(usuario.getNome());
		return userDetails;

	}

}
