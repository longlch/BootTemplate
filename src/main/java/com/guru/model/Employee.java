package com.guru.model;

import java.util.Set;

public class Employee {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int id;
	private Set<Car> cars;
	
	public Employee() {
		// TODO Auto-generated constructor stub
	}
	
	public String getName() {
		return name;
	}

	public Employee( int id,String name) {
		super();
		this.name = name;
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public Set<Car> getCars() {
		return cars;
	}

	public void setCars(Set<Car> cars) {
		this.cars = cars;
	}
	
}
