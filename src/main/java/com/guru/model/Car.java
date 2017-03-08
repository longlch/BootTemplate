package com.guru.model;

import java.util.Set;

public class Car {
	private String carName;
	private Employee employee;
	
	public Car() {
		// TODO Auto-generated constructor stub
	}
	
	public Car(String carName) {
		super();
		this.carName = carName;
	}
	public String getCarName() {
		return carName;
	}
	public void setCarName(String carName) {
		this.carName = carName;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	 
	
}
