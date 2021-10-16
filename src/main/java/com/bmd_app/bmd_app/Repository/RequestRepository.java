package com.bmd_app.bmd_app.Repository;

import com.bmd_app.bmd_app.Entity.Request;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface RequestRepository extends CrudRepository<Request, Long> {

	@Query(value = "SELECT r from Request r WHERE r.id = :id")
	ArrayList<Request> findAllBySingleId(@Param("id") Long id);
}
