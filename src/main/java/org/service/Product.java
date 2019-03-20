package org.service;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Product {

	private @Id @GeneratedValue Long id;	
	private String name;
	private String description;
	private Status status;
	
	public Product() {}
	
	

	public Product(String name, String description, Status status) {
		this.name = name;
		this.description = description;
		this.status = status;
	}
	
	
	
}
