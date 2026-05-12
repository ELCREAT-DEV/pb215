//ELCREAT TECHNOLOGIES
//Projeto: PB215 - Sistema de Gestão Acadêmica
//Software propedado da ELCREAT TECHNOLOGIES
//DATA DE CRIAÇÃO: 01/05/2024
//Autores: Elder Almeida


package com.pb215.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing

public class Pb215Application {

	public static void main(String[] args) {
		SpringApplication.run(Pb215Application.class, args);
	}

}
