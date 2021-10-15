package com.bmd_app.bmd_app.Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "T_REQUEST")
public class Request {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "SENDERADRESS")
	private String senderAddress;

	@Column(name = "MESSAGEBODY")
	private String messageBody;

	@Column(name = "ISSUCCESS")
	private Boolean isSuccess;

	@Column(name = "STARTTIME")
	private Date startTime;

	@Column(name = "ENDTIME")
	private Date endTime;

	@ManyToOne
	@JoinColumn(name="CLIENT", nullable=false)
	private Client client;

	@OneToMany(mappedBy="request", fetch=FetchType.LAZY)
	private List<Delivery> destinationNumbers = new ArrayList<>();

	public List<Delivery> getDestinationNumbers() {
		return destinationNumbers;
	}

	public void setDestinationNumbers(List<Delivery> destinationNumbers) {
		this.destinationNumbers = destinationNumbers;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public Boolean getSuccess() {
		return isSuccess;
	}

	public void setSuccess(Boolean success) {
		isSuccess = success;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
