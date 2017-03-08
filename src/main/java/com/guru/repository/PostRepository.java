package com.guru.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guru.model.Post1;

@Repository
public interface PostRepository extends JpaRepository<Post1,Integer>{

}
