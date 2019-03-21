package org.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

	private final ProductRepository repository;
	
	private final ProductResourceAssembler assembler;
	
	ProductController (ProductRepository repository, ProductResourceAssembler assembler){
		this.repository=repository;
		this.assembler=assembler;
	}
	
    @RequestMapping("/")
    public String helloWorld(HttpServletResponse response)  throws IOException{
    		  response.getWriter().println("Hello World");
    		  return "HOME PAGE";
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
	
	@PostMapping("/products")
	ResponseEntity<Resource<Product>> newProduct(@RequestBody Product product) {
		
		product.setStatus(Status.IN_PROGRESS);
		Product newProduct = repository.save(product);
		
		return ResponseEntity
				.created(linkTo(methodOn(ProductController.class).one(newProduct.getId())).toUri())
				.body(assembler.toResource(newProduct));
	}
	
	@PutMapping("/products/{id}/complete")
	ResponseEntity<ResourceSupport> complete(@PathVariable Long id) {
		
	Product product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
		
		if (product.getStatus() == Status.IN_PROGRESS) {
			product.setStatus(Status.COMPLETED);
			return ResponseEntity.ok(assembler.toResource(repository.save(product)));
		}
	
		return ResponseEntity
				.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body(new VndErrors.VndError("ERROR ","Method not allowed for status: " + product.getStatus()));
	
		
	}
	
	@DeleteMapping("/products/{id}/cancel")
	ResponseEntity<ResourceSupport> cancel(@PathVariable Long id) {
		
		Product product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
		
		if (product.getStatus() == Status.IN_PROGRESS) {
			product.setStatus(Status.CANCELLED);
			return ResponseEntity.ok(assembler.toResource(repository.save(product)));
	}
		
		return ResponseEntity
				.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body(new VndErrors.VndError("ERROR ","Method not allowed for status: " + product.getStatus()));
	
	}

}
