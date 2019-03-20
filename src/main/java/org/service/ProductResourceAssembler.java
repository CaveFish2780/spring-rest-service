package org.service;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@Component
public class ProductResourceAssembler implements ResourceAssembler<Product, Resource<Product>>{
	
	@Override
	public Resource<Product> toResource(Product product) {
		
		Resource<Product> productResource = new Resource<>(product,
				linkTo(methodOn(ProductController.class).one(product.getId())).withSelfRel(),
				linkTo(methodOn(ProductController.class).all()).withRel("products")
			);

	if(product.getStatus() == Status.IN_PROGRESS) {
		productResource.add(
				linkTo(methodOn(ProductController.class)
						.cancel(product.getId())).withRel("cancel"));
		productResource.add(
				linkTo(methodOn(ProductController.class)
					.complete(product.getId())).withRel("complete"));
	}
	
		return productResource;
	}


	
	
}



