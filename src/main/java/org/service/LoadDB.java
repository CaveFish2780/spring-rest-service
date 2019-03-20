package org.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class LoadDB {

	@Bean
	CommandLineRunner load(ProductRepository repository) {
		return args -> {
			repository.save(new Product("Book", "stock text", Status.IN_PROGRESS));
			repository.save(new Product("Floppy disk", "1MB", Status.IN_PROGRESS));
			repository.save(new Product("Book", "stock text", Status.IN_PROGRESS));
			repository.save(new Product("VHS tape", "stock footage", Status.IN_PROGRESS));
			repository.findAll().forEach(product -> {
			log.info(product.toString());
			});
		};
	}
}
