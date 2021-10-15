package com.bmd_app.bmd_app.Entity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Client {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "DAILYMESSAGEQUOTA")
	private Long dailyMessageQuota;

	@OneToMany(mappedBy="request", fetch=FetchType.LAZY, orphanRemoval=false)
	private List<Request> requestId = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDailyMessageQuota() {
		return dailyMessageQuota;
	}

	public void setDailyMessageQuota(Long dailyMessageQuota) {
		this.dailyMessageQuota = dailyMessageQuota;
	}

	public List<Request> getRequestId() {
		return requestId;
	}

	public void setRequestId(List<Request> requestId) {
		this.requestId = requestId;
	}
}
