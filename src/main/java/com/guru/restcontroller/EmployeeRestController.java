package com.guru.restcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guru.controller.EmpRestURLConsonants;
import com.guru.model.Car;
import com.guru.model.Employee;

@RestController
public class EmployeeRestController {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeRestController.class);
	
	Map<Integer, Employee> empData= new HashMap<Integer, Employee>();
	
	@RequestMapping(value=EmpRestURLConsonants.EMP_DUMMY,
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_VALUE
					)
	
	public @ResponseBody Employee healthCheck(){
		logger.info("Check web service");
		Employee empDum= new Employee(9999,"dummy guy");
		Set<Car> cars= new HashSet<Car>();
		Car car1= new Car("BMW");
		Car car2= new Car("Ford");
		
		cars.add(car1);
		cars.add(car2);
		empDum.setCars(cars);
		empData.put(empDum.getId(), empDum);
		return empDum;
	}
	@RequestMapping(value=EmpRestURLConsonants.EMP_NEW,method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Employee createEmployee(@RequestBody Employee emp){
		empData.put(emp.getId(),emp);
		return emp;
	}
	@RequestMapping(value="/rest/emps",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Employee> allEmp(){
		logger.info("LIST ALL");
		List<Employee> employees= new ArrayList<Employee>();
		Set<Integer> idSet= empData.keySet();
		Employee empDummy;
		for (Integer integer : idSet) {
			empDummy=empData.get(integer);
			employees.add(empDummy);
		}
		return employees;
	}
}
