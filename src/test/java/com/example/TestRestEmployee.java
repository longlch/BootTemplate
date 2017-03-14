package com.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.springframework.web.client.RestTemplate;
import org.thymeleaf.expression.Calendars;

import com.guru.controller.EmpRestURLConsonants;
import com.guru.model.Car;
import com.guru.model.Employee;

public class TestRestEmployee {
	
	private static final String SERVER_URI="http://localhost:8080/";
	
	public static void main(String[] args) {
		System.out.println("start");
		testDummyEmp();
		System.out.println("***************");
		System.out.println("create new employee");
		testNewEmployee();
		System.out.println("***************");
		System.out.println("find by id");
		testFindEmpById();
		System.out.println("***************");
		System.out.println("specific employee");
		testEmpsInSpecific();
		System.out.println("end");
	}
	
	public static void testDummyEmp(){
		RestTemplate restTemplate= new RestTemplate();
		Employee empResp= restTemplate.getForObject(SERVER_URI+EmpRestURLConsonants.EMP_DUMMY, Employee.class);
		printEmp(empResp);
		
	}
	private static void testNewEmployee(){
		RestTemplate restTemplate = new RestTemplate();
		Employee emp= new Employee(123,"tyra");
		Set<Car> cars= new HashSet<Car>();
		Car car1= new Car("BMW");
		Car car2= new Car("Ford");
		cars.add(car1);
		cars.add(car2);
		emp.setCars(cars);
//		convert Tyra object to Json than get back json string
		Employee empReponse= restTemplate.postForObject(SERVER_URI+EmpRestURLConsonants.EMP_NEW,emp,Employee.class);
		printEmp(empReponse);
	}
	
	public static void testEmpsInSpecific(){
		RestTemplate restTemplate= new RestTemplate();
		List<LinkedHashMap> emps=  restTemplate.getForObject(SERVER_URI+EmpRestURLConsonants.EMPS, List.class);
		List<LinkedHashMap> cars;
		for (LinkedHashMap map: emps) {
			cars=(List<LinkedHashMap>) map.get("cars");
			System.out.println("id "+map.get("id")+" name "+map.get("name"));
			for (LinkedHashMap car : cars) {
				System.out.println("car name "+ car.get("carName"));
			}
			System.out.println("======");
		}
	}
	private static void testGetAllEmployee(){
		RestTemplate restTemplate = new RestTemplate();
		List<LinkedHashMap> emps = restTemplate.getForObject(SERVER_URI+EmpRestURLConsonants.EMPS, List.class);
		for (LinkedHashMap map : emps) {
			System.out.println("id "+map.get("id")+" name "+map.get("name"));
		}
	}
	public static void testSpecificEmployee(){
		RestTemplate restTemplate = new RestTemplate();
		List<LinkedHashMap> emps= restTemplate.getForObject(SERVER_URI+EmpRestURLConsonants.EMPS, List.class);
		for (LinkedHashMap map : emps) {
			System.out.println("id "+map.get("id")+" name "+map.get("name")+" cars "+map.get("cars"));
		}
	}
	public static void testFindEmpById(){
		RestTemplate restTemplate = new RestTemplate();
		Employee emp= restTemplate.getForObject(SERVER_URI+"rest/emp/{id}", Employee.class,"9999");
		printEmp(emp);
	}
	
	public static void printEmp(Employee emp){
		System.out.println("id "+emp.getId()+" name: "+emp.getName());
	}
}
