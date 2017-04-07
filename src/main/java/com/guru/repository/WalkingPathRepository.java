package com.guru.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guru.model.WalkingPath;
import com.guru.model.WalkingPathId;

@Repository
public interface WalkingPathRepository extends JpaRepository<WalkingPath, WalkingPathId>{

}
