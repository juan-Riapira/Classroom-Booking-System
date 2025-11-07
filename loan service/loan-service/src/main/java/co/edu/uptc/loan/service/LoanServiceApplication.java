package co.edu.uptc.loan.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import java.time.Duration;

@SpringBootApplication
public class LoanServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanServiceApplication.class, args);
	}

	// Configuración básica de RestTemplate con timeouts
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplateBuilder()
			.connectTimeout(Duration.ofSeconds(3))  // Máximo 3 segundos para conectar
			.readTimeout(Duration.ofSeconds(5))     // Máximo 5 segundos para leer respuesta
			.build();
	}

}
