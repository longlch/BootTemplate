package com.guru.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guru.model.BusPath;
import com.guru.model.BusPathId;

@Repository
public interface BusPathRepository extends JpaRepository<BusPath,BusPathId>{

}
