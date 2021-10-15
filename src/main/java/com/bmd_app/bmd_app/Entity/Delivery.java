package com.bmd_app.bmd_app.Entity;

import javax.persistence.*;

@Entity
public class Delivery {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "DESTINATIONNUMBER")
	private String destinationNumber;

	@Column(name = "ISSUCCESS")
	private Boolean isSuccess;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDestinationNumber() {
		return destinationNumber;
	}

	public void setDestinationNumber(String destinationNumber) {
		this.destinationNumber = destinationNumber;
	}

	public Boolean getSuccess() {
		return isSuccess;
	}

	public void setSuccess(Boolean success) {
		isSuccess = success;
	}
}
