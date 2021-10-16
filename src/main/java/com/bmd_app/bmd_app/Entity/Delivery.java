package com.bmd_app.bmd_app.Entity;

import javax.persistence.*;

@Entity
@Table(name = "T_DELIVERY")
public class Delivery {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "DESTINATIONNUMBER")
	private String destinationNumber;

	@Column(name = "ISSUCCESS")
	private Boolean isSuccess;

	@ManyToOne
	@JoinColumn(name="REQUEST", nullable=false)
	private Request request;

	@Column(name = "ISCANCELLED")
	private Boolean isCancelled;

	@Column(name = "RESULTCODE")
	private Long resultCode;

	public Boolean getCancelled() {
		return isCancelled;
	}

	public void setCancelled(Boolean cancelled) {
		isCancelled = cancelled;

	}

	public Long getResultCode() {
		return resultCode;
	}

	public void setResultCode(Long resultCode) {
		this.resultCode = resultCode;
	}

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

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}
}
