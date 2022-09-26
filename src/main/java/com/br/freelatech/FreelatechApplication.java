package com.br.freelatech;

import com.br.freelatech.validators.UsuarioValidator;
import java.util.Locale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.validation.Validator;

@SpringBootApplication
public class FreelatechApplication implements ApplicationRunner {

	private static final Logger logger = LogManager.getLogger(FreelatechApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FreelatechApplication.class, args);

	}

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		logger.debug("DEBUG");
		logger.info("INFO");
		logger.warn("WARN");
		logger.error("ERROR");
		logger.fatal("FATAL.");
	}

	@Value("${freelatech.locale.default}")
	private String defaultLocale;

	@Bean
	public Validator usuarioValidator() {
		return new UsuarioValidator();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public LocaleResolver localeResolver() {

		SessionLocaleResolver slr = new SessionLocaleResolver();

		Locale locale = new Locale(defaultLocale);
		slr.setDefaultLocale(locale);

		return slr;
	}

	@Bean
	public WebMvcConfigurer configurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				LocaleChangeInterceptor l = new LocaleChangeInterceptor();
				l.setParamName("locale");
				registry.addInterceptor(l);
			}
		};
	}
}
