package org.service;

public class ProductNotFoundException extends RuntimeException{

	ProductNotFoundException(Long id){
		super("Product: " + id + " not found");
	}
}
