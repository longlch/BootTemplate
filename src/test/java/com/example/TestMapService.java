package com.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.guru.service.IMapService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMapService {
	
	@Autowired
	IMapService service;
	
	@Test
	public void testGetRouteName(){
		String a=service.getRouteName(5);
		assertEquals("Tuyến số 5: Nguyễn Tất Thành- Xuân Diệu ", a);
	}
}
