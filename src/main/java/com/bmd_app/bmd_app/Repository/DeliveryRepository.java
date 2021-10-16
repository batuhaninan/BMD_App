package com.bmd_app.bmd_app.Repository;

import com.bmd_app.bmd_app.Entity.Delivery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface DeliveryRepository  extends CrudRepository<Delivery, Long> {

    @Query(value = "SELECT r from Delivery r WHERE r.request.client.id = :id")
    ArrayList<Delivery> findAllBySingleId(@Param("id") Long id);
}
