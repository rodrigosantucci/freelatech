package com.br.freelatech.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.br.freelatech.models.Usuario;
import com.br.freelatech.repositories.UsuarioRepository;


public class UsuarioValidator implements Validator{
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	
	@Override
	public boolean supports(Class<?> aClass) {
		return Usuario.class.equals(aClass);
	}
	
	
	
    @Override
    public void validate(Object o, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"nome", "usuario.nome.vazio", "É necessário preencher o campo nome");


        if( o instanceof Usuario != true) {
            errors.reject("usuario.invalid-class");
            return ;
        }
        Usuario usuario = (Usuario) o;
        //
        // Check if password is empty or too short

        if(usuario.getSenha().length() < 6){
            errors.rejectValue("senha", "usuario.senha.curta", "A senha deve possuir ao menos 5 caracteres");
        }


        //
        // Check if email already exists:
        if( usuarioRepository.findByEmail(usuario.getEmail()) != null){
            errors.rejectValue("email", "usuario.email.existe", "Email já em uso.");
        }

    }
}
