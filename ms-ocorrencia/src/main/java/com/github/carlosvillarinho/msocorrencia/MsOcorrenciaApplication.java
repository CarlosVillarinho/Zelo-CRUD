package com.github.carlosvillarinho.msocorrencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsOcorrenciaApplication {

	public static void main(String[] args) {

		SpringApplication.run(MsOcorrenciaApplication.class, args);
	}

}
