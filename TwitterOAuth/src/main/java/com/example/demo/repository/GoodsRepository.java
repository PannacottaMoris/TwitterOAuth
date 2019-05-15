package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.GoodsEntity;


public interface GoodsRepository extends JpaRepository<GoodsEntity, Integer>, JpaSpecificationExecutor{
	    public List<GoodsEntity> findById(int id);

	    @Query("from GoodsEntity s where s.name = ?1")
	    public List<GoodsEntity> findByName(String name);

		public void deleteById(int id);
}
