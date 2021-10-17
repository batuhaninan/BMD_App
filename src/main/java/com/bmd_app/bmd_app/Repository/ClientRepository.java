package com.bmd_app.bmd_app.Repository;

import com.bmd_app.bmd_app.Entity.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClientRepository  extends CrudRepository<Client, Long> {

	Optional<Client> findByEmail(String email);

}
