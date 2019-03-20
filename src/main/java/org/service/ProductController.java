package org.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

	private final ProductRepository repository;
	
	private final ProductResourceAssembler assembler;
	
	ProductController (ProductRepository repository, ProductResourceAssembler assembler){
		this.repository=repository;
		this.assembler=assembler;
	}
	
	@GetMapping("/products/{id}")
	Resource<Product> one(@PathVariable Long id) {
		
		Product product = repository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException(id));
		
		return assembler.toResource(product);
	}
	
	@GetMapping("/products")
	Resources<Resource<Product>> all() {

		List<Resource<Product>> products = repository.findAll().stream()
			.map(assembler::toResource)
			.collect(Collectors.toList());

		return new Resources<>(products,
			linkTo(methodOn(ProductController.class).all()).withSelfRel());
	}
}
