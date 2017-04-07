package com.guru.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guru.model.BusRoute;

@Repository
public interface BusRouteRepository extends JpaRepository<BusRoute,Integer>{
	
}
