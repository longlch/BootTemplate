package com.guru.model;

import java.util.ArrayList;
import java.util.List;

public class Student {
	private String name;
	private List< Bank> banks= new ArrayList<>();
	
	public Student() {
		// TODO Auto-generated constructor stub
	}

	public Student(String name, List<Bank> banks) {
		super();
		this.name = name;
		this.banks = banks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Bank> getBanks() {
		return banks;
	}

	public void setBanks(List<Bank> banks) {
		this.banks = banks;
	}
	
}
